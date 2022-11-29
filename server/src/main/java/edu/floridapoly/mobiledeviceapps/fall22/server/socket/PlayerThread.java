package edu.floridapoly.mobiledeviceapps.fall22.server.socket;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.SocketMessageGenerator;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.ActiveGame;

public class PlayerThread extends Thread{

    private static final String MAGIC_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    private final TriviaChanceServer server;
    private final Socket socket;

    private DataOutputStream out;

    public PlayerThread(TriviaChanceServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            this.out = new DataOutputStream(this.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            InputStream in = this.getSocket().getInputStream();

            boolean open = true;
            do {
                System.out.println("Waiting for message:");
                String message = IOUtils.toString(in, StandardCharsets.UTF_8);

                System.out.println("Got message:" + message);
                if(message == null) continue;

                if(message.startsWith("GET")) {
                    this.respond(message);
                    continue;
                }

                SocketMessageGenerator generator = SocketMessageGenerator.fromString(message);
                System.out.println("Got message " + generator.generate());
                switch(generator.getType()) {
                    case JOIN_GAME -> {
                        String profileUUID = generator.getParams().get("profileUUID");
                        String gameUUID = generator.getParams().get("gameUUID");

                        ActiveGame game = this.getServer().getGameHandler().findGame(gameUUID);
                        game.addPlayer(new Player(this.getServer().getProfileContainer().retrieve(Profile.class, "profiles." + profileUUID)), this.getSocket());
                    }

                    case LEAVE_GAME -> {
                        String profileUUID = generator.getParams().get("profileUUID");
                        String gameUUID = generator.getParams().get("gameUUID");

                        this.getServer().getGameHandler().findGame(gameUUID).removePlayer(profileUUID);

                        open = false;
                    }
                }
            } while(open);

            this.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respond(String httpMessage) {
        String clientKey = httpMessage.split("\n")[3].split(":")[1].substring(1);
        String hashed = clientKey + MAGIC_STRING;
        String key = Base64.getEncoder().encodeToString(DigestUtils.sha1Hex(hashed).getBytes(StandardCharsets.UTF_8));

        String response =
                "HTTP/1.1 101 Switching Protocols" + "\r\n" +
                "Upgrade: websocket" + "\r\n" +
                "Connection: Upgrade" + "\r\n" +
                "Sec-WebSocket-Accept: " + key + "\r\n";

        System.out.println("Sending message: " + response);
        this.writeMessage(response);
    }

    private void writeMessage(String message) {
        try {
            this.out.writeChars(message);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public TriviaChanceServer getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }
}

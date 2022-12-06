package edu.floridapoly.mobiledeviceapps.fall22.server.socket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.MessageType;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.SocketMessageGenerator;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.ActiveGame;

public class TriviaChanceWebSocket extends WebSocketServer {

    private final TriviaChanceServer server;

    public TriviaChanceWebSocket(TriviaChanceServer server, InetSocketAddress socketAddress) {
        super(socketAddress);
        this.server = server;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {}

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        for(ActiveGame game : this.getServer().getGameHandler().getActiveGames()) {
            game.getPlayers().entrySet().removeIf(entry -> entry.getValue().equals(conn));
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        SocketMessageGenerator generator = SocketMessageGenerator.fromString(message);

        switch (generator.getType()) {
            case JOIN_GAME -> {
                String profileUUID = generator.getParams().get("profileUUID");
                String gameUUID = generator.getParams().get("gameUUID");

                ActiveGame game = this.getServer().getGameHandler().findGame(UUID.fromString(gameUUID));
                game.addPlayer(new Player(this.getServer().getProfileContainer().retrieve(Profile.class, "profiles." + profileUUID)), conn);

                //Update all other players of this new player joining the game, and this player of all other players.
                for (Map.Entry<Player, WebSocket> entry : game.getPlayers().entrySet()) {
                    if(conn != entry.getValue()) {
                        conn.send(new SocketMessageGenerator(MessageType.UPDATE_PLAYER)
                                .setParam("state", "1")
                                .setParam("profileUUID", entry.getKey().getProfile().getUUID().toString())
                                .setParam("gameUUID", gameUUID).generate());
                    }
                    entry.getValue().send(new SocketMessageGenerator(MessageType.UPDATE_PLAYER)
                            .setParam("state", "1")
                            .setParam("profileUUID", profileUUID)
                            .setParam("gameUUID", gameUUID).generate());
                }
            }

            case LEAVE_GAME -> {
                String profileUUID = generator.getParams().get("profileUUID");
                String gameUUID = generator.getParams().get("gameUUID");

                ActiveGame game = this.getServer().getGameHandler().findGame(UUID.fromString(gameUUID));
                game.removePlayer(profileUUID);

                //Update all other players of this player leaving the game.
                for (Map.Entry<Player, WebSocket> entry : game.getPlayers().entrySet()) {
                    entry.getValue().send(new SocketMessageGenerator(MessageType.UPDATE_PLAYER)
                            .setParam("state", "0")
                            .setParam("profileUUID", profileUUID)
                            .setParam("gameUUID", gameUUID).generate());
                }
            }

            case HOST_START_GAME -> {
                String gameUUID = generator.getParams().get("gameUUID");

                ActiveGame game = this.getServer().getGameHandler().findGame(UUID.fromString(gameUUID));

                //Update all other players that the game has started.
                for (Map.Entry<Player, WebSocket> entry : game.getPlayers().entrySet()) {
                    entry.getValue().send(new SocketMessageGenerator(MessageType.START_GAME)
                            .setParam("gameUUID", gameUUID).generate());
                }

                game.nextQuestion();
                game.setStarted(true);
            }

            case KICK_PLAYER -> {
                String profileUUID = generator.getParams().get("profileUUID");
                String gameUUID = generator.getParams().get("gameUUID");

                ActiveGame game = this.getServer().getGameHandler().findGame(UUID.fromString(gameUUID));

                game.getWebSocket(game.findPlayer(profileUUID)).send(new SocketMessageGenerator(MessageType.KICKED)
                        .setParam("profileUUID", profileUUID)
                        .setParam("gameUUID", gameUUID).generate());

                game.removePlayer(profileUUID);

                //Update all other players of this player leaving the game.
                for (Map.Entry<Player, WebSocket> entry : game.getPlayers().entrySet()) {
                    entry.getValue().send(new SocketMessageGenerator(MessageType.UPDATE_PLAYER)
                            .setParam("state", "0")
                            .setParam("profileUUID", profileUUID)
                            .setParam("gameUUID", gameUUID).generate());
                }
            }

            case SUBMIT_QUESTION -> {
                String profileUUID = generator.getParams().get("profileUUID");
                String gameUUID = generator.getParams().get("gameUUID");
                int questionId = Integer.parseInt(generator.getParams().get("questionId"));
                boolean correct = Boolean.parseBoolean(generator.getParams().get("correct"));

                ActiveGame game = this.getServer().getGameHandler().findGame(UUID.fromString(gameUUID));
                Player player = game.findPlayer(profileUUID);

                if(correct) {
                    player.getStats().setCorrect(player.getStats().getCorrect() + 1);
                } else {
                    player.getStats().setIncorrect(player.getStats().getIncorrect() + 1);
                }

                game.submit(player, questionId);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started on port 8083");
    }

    public TriviaChanceServer getServer() {
        return server;
    }
}

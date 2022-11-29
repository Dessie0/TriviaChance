package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.MessageType;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.SocketMessageGenerator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;


/**
 * Sends Serverbound socket messages to interface with the server about the current game.
 */
public class TriviaSocketInterface {

    private final TriviaChanceAPI api;
    private final WebSocket socket;

    TriviaSocketInterface(TriviaChanceAPI api) {
        this.api = api;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + TriviaChanceAPI.SERVER_IP + ":8083/")
                .build();

        this.socket = client.newWebSocket(request, new TriviaWebSocket(this.getAPI()));
    }

    public void joinGame(Profile profile, TriviaGame game) {
        SocketMessageGenerator generator = new SocketMessageGenerator(MessageType.JOIN_GAME);
        generator.setParam("profileUUID", profile.getUUID().toString());
        generator.setParam("gameUUID", game.getUUID().toString());

        this.getSocket().send(generator.generate());
    }

    public void leaveGame(Profile profile, TriviaGame game) {
        SocketMessageGenerator generator = new SocketMessageGenerator(MessageType.LEAVE_GAME);
        generator.setParam("profileUUID", profile.getUUID().toString());
        generator.setParam("gameUUID", game.getUUID().toString());

        this.getSocket().send(generator.generate());
    }

    public void startGame(TriviaGame game) {
        SocketMessageGenerator generator = new SocketMessageGenerator(MessageType.HOST_START_GAME);
        generator.setParam("gameUUID", game.getUUID().toString());

        this.getSocket().send(generator.generate());
    }

    public WebSocket getSocket() {
        return socket;
    }
    public TriviaChanceAPI getAPI() {
        return api;
    }
}

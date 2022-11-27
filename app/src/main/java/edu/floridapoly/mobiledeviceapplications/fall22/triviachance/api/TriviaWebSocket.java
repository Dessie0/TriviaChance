package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import java.util.UUID;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.PlayerJoinGameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.PlayerLeaveGameEvent;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.SocketMessageGenerator;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class TriviaWebSocket extends WebSocketListener {

    private final TriviaChanceAPI api;

    public TriviaWebSocket(TriviaChanceAPI api) {
        this.api = api;
    }


    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        System.out.println("Closed because: " + reason);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);

        SocketMessageGenerator generator = SocketMessageGenerator.fromString(text);
        switch(generator.getType()) {
            case UPDATE_PLAYER: {
                boolean state = generator.getParams().get("state").equals("1");
                String profileUUID = generator.getParams().get("profileUUID");

                if(state) {
                    this.getAPI().retrieveProfile(UUID.fromString(profileUUID)).thenAccept(profile -> {
                        this.getAPI().fireEvent(new PlayerJoinGameEvent(this.getAPI().getCurrentGame(), profile));
                    });
                } else {
                    this.getAPI().retrieveProfile(UUID.fromString(profileUUID)).thenAccept(profile -> {
                        this.getAPI().fireEvent(new PlayerLeaveGameEvent(this.getAPI().getCurrentGame(), profile));
                    });
                }

                break;
            }
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
        super.onFailure(webSocket, t, response);
    }

    public TriviaChanceAPI getAPI() {
        return api;
    }
}

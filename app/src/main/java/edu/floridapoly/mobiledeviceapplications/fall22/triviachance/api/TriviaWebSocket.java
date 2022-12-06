package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import com.google.gson.Gson;

import java.util.UUID;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.KickedFromGameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.NextQuestionEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.PlayerJoinGameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.PlayerLeaveGameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.StartGameEvent;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.TextQuestion;
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

            case START_GAME: {
                //If the UUID received and the UUID of the current game don't match, don't start it.
                if(!generator.getParams().get("gameUUID").equalsIgnoreCase(this.getAPI().getCurrentGame().getUUID().toString())) return;

                this.getAPI().fireEvent(new StartGameEvent(this.getAPI().getCurrentGame()));
                break;
            }

            case KICKED: {
                this.getAPI().fireEvent(new KickedFromGameEvent(this.getAPI().getCurrentGame()));
                break;
            }

            case NEXT_QUESTION: {
                //If the UUID received and the UUID of the current game don't match, dont go to the next question
                if(!generator.getParams().get("gameUUID").equalsIgnoreCase(this.getAPI().getCurrentGame().getUUID().toString())) return;

                //TODO Would need to add support for different question types
                TextQuestion question = new Gson().fromJson(generator.getParams().get("question"), TextQuestion.class);
                int questionId = Integer.parseInt(generator.getParams().get("questionId"));

                this.getAPI().fireEvent(new NextQuestionEvent(this.getAPI().getCurrentGame(), question, questionId));
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

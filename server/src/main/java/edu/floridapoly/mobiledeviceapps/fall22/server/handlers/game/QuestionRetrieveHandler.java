package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.ActiveGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class QuestionRetrieveHandler extends TriviaChanceHandler {
    public QuestionRetrieveHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String questionIndex = params.get("questionIndex");
        String gameUuid = params.get("gameUUID");
        String type = params.get("type");

        if(type.equalsIgnoreCase("text")) {
            ActiveGame game = this.getServer().getGameHandler().findGame(UUID.fromString(gameUuid));
            game.getQuestionRandomizer().getQuestion(Integer.parseInt(questionIndex)).thenAccept(question -> {
                try {
                    this.sendResponse(exchange, new Gson().toJson(question));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

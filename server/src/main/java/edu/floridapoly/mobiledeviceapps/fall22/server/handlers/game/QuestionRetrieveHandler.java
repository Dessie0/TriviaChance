package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class QuestionRetrieveHandler extends TriviaChanceHandler {
    public QuestionRetrieveHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String gameUuid = params.get("gameUUID");



    }
}

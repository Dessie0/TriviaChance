package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.ActiveGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class JoinGameHandler extends TriviaChanceHandler {
    public JoinGameHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String code = params.get("code");

        ActiveGame game = this.getServer().getGameHandler().findGame(code);

        if(game == null || game.isStarted()) {
            this.sendResponse(exchange, "null");
            return;
        }

        this.sendResponse(exchange, new Gson().toJsonTree(game.getGame()).getAsJsonObject());
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.ActiveGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class GameLeaderboardHandler extends TriviaChanceHandler {
    public GameLeaderboardHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String gameUuid = params.get("gameUUID");
        ActiveGame game = this.getServer().getGameHandler().findGame(UUID.fromString(gameUuid));

        if(game != null) {
            this.sendResponse(exchange, new Gson().toJson(game.getPlayers().keySet()));
        } else {
            this.sendResponse(exchange, "null");
        }
    }
}

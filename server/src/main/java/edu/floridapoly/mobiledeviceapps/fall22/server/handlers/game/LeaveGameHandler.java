package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class LeaveGameHandler extends TriviaChanceHandler {

    public LeaveGameHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String profileUuid = params.get("profileUUID");
        String gameUuid = params.get("gameUUID");

        TriviaGame game = this.getServer().getGameHandler().findGame(gameUuid);

        if(game == null) {
            this.sendResponse(exchange, "false");
            return;
        }

        this.getServer().getGameHandler().removePlayer(game, profileUuid);
        this.sendResponse(exchange, "true");
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.ActiveGame;
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

        ActiveGame game = this.getServer().getGameHandler().findGame(gameUuid);

        if(game == null) {
            this.sendResponse(exchange, "false");
            return;
        }

        game.removePlayer(profileUuid);
        if(game.getPlayers().isEmpty()) {
            this.getServer().getGameHandler().stopGame(game);
        }

        this.sendResponse(exchange, "true");
    }
}

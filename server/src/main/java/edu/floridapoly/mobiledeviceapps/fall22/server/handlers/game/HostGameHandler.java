package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class HostGameHandler extends TriviaChanceHandler {
    public HostGameHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TriviaGame game = new TriviaGame(UUID.randomUUID().toString(), TriviaGame.getRandomCode());
        this.getServer().getGameHandler().startGame(game);
        this.sendResponse(exchange, new Gson().toJsonTree(game).getAsJsonObject());
    }
}

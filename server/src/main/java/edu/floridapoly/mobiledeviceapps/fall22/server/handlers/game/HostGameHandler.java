package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.ActiveGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class HostGameHandler extends TriviaChanceHandler {
    public HostGameHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String profileUUID = params.get("profileUUID");
        TriviaGame game = new TriviaGame(UUID.randomUUID().toString(), TriviaGame.getRandomCode());

        ActiveGame activeGame = this.getServer().getGameHandler().startGame(game);
        activeGame.addPlayer(new Player(this.getServer().getProfileContainer().retrieve(Profile.class, "profiles." + profileUUID)));

        this.sendResponse(exchange, new Gson().toJsonTree(game).getAsJsonObject());
    }
}

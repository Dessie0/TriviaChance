package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class JoinGameHandler extends TriviaChanceHandler {
    public JoinGameHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String uuid = params.get("profileUUID");
        String code = params.get("code");

        TriviaGame game = this.getServer().getGameHandler().findGame(code);

        if(game == null) {
            this.sendResponse(exchange, "null");
            return;
        }

        this.getServer().getGameHandler().addPlayer(game, new Player(this.getServer().getProfileContainer().retrieve(Profile.class, "profiles." + uuid)));
        this.sendResponse(exchange, new Gson().toJsonTree(game).getAsJsonObject());
    }
}

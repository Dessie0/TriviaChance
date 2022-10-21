package edu.floridapoly.mobiledeviceapps.fall22.server.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;

public class ProfileRegisterHandler extends TriviaChanceHandler {
    public ProfileRegisterHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> params = this.getParams(exchange.getRequestURI().getQuery());
        String uuid = params.get("uuid");
        String username = params.get("username");

        Profile profile = new Profile(UUID.fromString(uuid), username);

        this.getServer().getProfileContainer().store("profiles." + uuid, profile)
                .thenRun(() -> {
                    try {
                        JsonObject object = new JsonObject();
                        object.addProperty("boolean", true);
                        this.sendResponse(exchange, object);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.server.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;

public class ProfileRetrieveHandler extends TriviaChanceHandler {
    public ProfileRetrieveHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> params = this.getParams(exchange.getRequestURI().getQuery());
        String uuid = params.get("uuid");

        this.getServer().getProfileContainer().retrieveAsync(Profile.class, "profiles." + uuid)
                .thenAccept(profile -> {
                    try {
                        if(profile == null) {
                            this.sendNotFoundError(exchange, "No profile found by uuid " + uuid);
                            return;
                        }

                        JsonObject object = new JsonObject();
                        object.addProperty("uuid", profile.getUUID().toString());
                        object.addProperty("username", profile.getUsername());
                        this.sendResponse(exchange, object);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}

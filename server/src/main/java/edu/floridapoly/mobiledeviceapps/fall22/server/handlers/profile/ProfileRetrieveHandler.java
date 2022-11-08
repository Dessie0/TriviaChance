package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class ProfileRetrieveHandler extends TriviaChanceHandler {
    public ProfileRetrieveHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> params = this.getParams(exchange);
        String profileUUID = params.get("profileUUID");

        JsonObject object = this.getServer().getProfileContainer().getObject().get("profiles")
                .getAsJsonObject().get(profileUUID).getAsJsonObject();
        try {
            if(object != null) {
                this.sendResponse(exchange, object);
            } else {
                this.sendNotFoundError(exchange, "No profile found by uuid " + profileUUID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
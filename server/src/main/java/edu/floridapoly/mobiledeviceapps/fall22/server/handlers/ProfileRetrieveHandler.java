package edu.floridapoly.mobiledeviceapps.fall22.server.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;

public class ProfileRetrieveHandler extends TriviaChanceHandler {
    public ProfileRetrieveHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> params = this.getParams(exchange.getRequestURI().getQuery());
        String uuid = params.get("uuid");

        JsonObject object = this.getServer().getProfileContainer().getObject().get("profiles")
                .getAsJsonObject().get(uuid).getAsJsonObject();

        System.out.println("Sending " + object);

        try {
            if(object == null) {
                this.sendNotFoundError(exchange, "No profile found by uuid " + uuid);
            } else {
                this.sendResponse(exchange, object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

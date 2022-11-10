package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile;

import com.sun.net.httpserver.HttpExchange;

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

        this.sendProfileResponse(exchange, profileUUID);
    }

}

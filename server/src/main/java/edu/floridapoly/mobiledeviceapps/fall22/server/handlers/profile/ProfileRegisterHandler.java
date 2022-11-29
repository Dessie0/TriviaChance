package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class ProfileRegisterHandler extends TriviaChanceHandler {
    public ProfileRegisterHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> params = this.getParams(exchange);
        String profileUUID = params.get("profileUUID");
        String username = params.get("username");

        Profile profile = new Profile(UUID.fromString(profileUUID), username, null, new ArrayList<>(), 0);

        this.getServer().getProfileContainer().store("profiles." + profileUUID, profile)
                .thenRun(() -> {
                    try {
                        this.sendResponse(exchange, "true");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}

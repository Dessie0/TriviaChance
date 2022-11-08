package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class ProfileUpdateUsernameHandler extends TriviaChanceHandler {
    public ProfileUpdateUsernameHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String profileUUID = params.get("profileUUID");
        String username = params.get("username");

        Profile profile = this.getServer().getProfileContainer().retrieve(Profile.class, "profiles." + profileUUID);
        profile.setUsername(username);

        this.getServer().getProfileContainer().store("profiles." + profileUUID, profile).thenRun(() -> {
            this.sendProfileResponse(exchange, profileUUID);
        });
    }
}

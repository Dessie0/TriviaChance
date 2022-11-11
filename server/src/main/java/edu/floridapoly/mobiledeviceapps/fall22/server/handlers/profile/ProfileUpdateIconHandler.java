package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class ProfileUpdateIconHandler extends TriviaChanceHandler {
    public ProfileUpdateIconHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String profileUUID = params.get("profileUUID");
        String iconURL = params.get("iconURL");

        Profile profile = this.getServer().getProfileContainer().retrieve(Profile.class, "profiles." + profileUUID);
        profile.setIconURL(iconURL);

        this.getServer().getProfileContainer().store("profiles." + profileUUID, profile).thenRun(() -> {
            if(profile.getIconURL() == null) {
                this.getServer().getProfileContainer().delete("profiles." + profileUUID + ".iconURL")
                        .thenRun(() -> this.sendProfileResponse(exchange, profileUUID));
            } else {
                this.sendProfileResponse(exchange, profileUUID);
            }
        });
    }
}

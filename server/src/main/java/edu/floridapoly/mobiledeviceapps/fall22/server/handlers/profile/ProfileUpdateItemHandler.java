package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class ProfileUpdateItemHandler extends TriviaChanceHandler {
    public ProfileUpdateItemHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String profileUUID = params.get("profileUUID");
        int itemId = Integer.parseInt(params.get("itemId"));
        int quantity = Integer.parseInt(params.get("quantity"));

        Profile profile = this.getServer().getProfileContainer().retrieve(Profile.class, "profiles." + profileUUID);

        if(profile.getInventory().stream().noneMatch(item -> item.getItemID() == itemId)) {
            profile.getInventory().add(new item(itemId, quantity));
        } else {
            profile.getInventory().stream()
                    .filter(item -> item.getItemID() == itemId)
                    .findFirst().ifPresent(item -> item.setQuantity(quantity));
        }

        this.getServer().getProfileContainer().store("profiles." + profileUUID, profile).thenRun(() -> {
            this.sendProfileResponse(exchange, profileUUID);
        });
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.api.profile;

import java.util.UUID;

public class Profile {

    private final UUID uuid;
    private String username;

    public Profile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public UUID getUUID() {
        return uuid;
    }
    public String getUsername() {
        return username;
    }
}

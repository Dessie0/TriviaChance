package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile;

import java.util.UUID;

public class Profile {

    private final UUID id;
    private String username;

    public Profile(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}

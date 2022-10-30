package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.item;
import java.util.List;
import java.util.UUID;


public class Profile {

    private final UUID id;
    private String username;
    static public List<item> inventory;


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

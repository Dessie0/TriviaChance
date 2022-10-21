package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class Player {

    private final Profile profile;

    public Player(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}

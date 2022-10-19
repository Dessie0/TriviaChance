package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.gameplay;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile.Profile;

public class Player {

    private final Profile profile;

    public Player(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class Player {

    private final Profile profile;
    private final GameStats stats;

    public Player(Profile profile) {
        this.profile = profile;
        this.stats = new GameStats();
    }

    public Profile getProfile() {
        return profile;
    }
    public GameStats getStats() {
        return stats;
    }
}

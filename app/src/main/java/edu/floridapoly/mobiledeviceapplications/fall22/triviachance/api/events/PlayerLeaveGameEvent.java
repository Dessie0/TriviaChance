package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class PlayerLeaveGameEvent extends GameEvent {
    private final Profile profile;

    public PlayerLeaveGameEvent(TriviaGame game, Profile profile) {
        super(game);
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}

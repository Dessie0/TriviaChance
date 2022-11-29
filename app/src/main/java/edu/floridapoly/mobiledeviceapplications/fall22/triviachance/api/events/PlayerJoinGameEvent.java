package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class PlayerJoinGameEvent extends GameEvent {

    private final Profile profile;

    public PlayerJoinGameEvent(TriviaGame game, Profile profile) {
        super(game);
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}

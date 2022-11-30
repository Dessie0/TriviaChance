package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class KickedFromGameEvent extends GameEvent {
    public KickedFromGameEvent(TriviaGame game) {
        super(game);
    }
}

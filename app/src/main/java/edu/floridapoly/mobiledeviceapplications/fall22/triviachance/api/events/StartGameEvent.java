package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class StartGameEvent extends GameEvent {
    public StartGameEvent(TriviaGame game) {
        super(game);
    }
}

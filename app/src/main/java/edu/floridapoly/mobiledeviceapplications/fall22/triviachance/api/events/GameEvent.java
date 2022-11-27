package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public abstract class GameEvent {
    private final TriviaGame game;

    public GameEvent(TriviaGame game) {
        this.game = game;
    }

    public TriviaGame getGame() {
        return game;
    }
}

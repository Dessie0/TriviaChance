package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class PlayerJoinGameEvent extends GameEvent {

    private Player player;
    private TriviaGame game;

    public PlayerJoinGameEvent(TriviaGame game, Player player) {
        super(game);
        this.player = player;
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class GameHandler {

    private final Map<TriviaGame, List<Player>> activeGames = new HashMap<>();
    private final QuestionRandomizer questionRandomizer;

    public GameHandler() {
        this.questionRandomizer = new QuestionRandomizer();
    }

    public void addPlayer(TriviaGame game, Player player) {
        //Add the player into the game, and create the game if it does not exist.
        this.getActiveGames().putIfAbsent(game, new ArrayList<>());
        this.getActiveGames().get(game).add(player);
    }

    public void removePlayer(TriviaGame game, String uuid) {
        if(!this.getActiveGames().containsKey(game)) return;

        //Remove the player from the game.
        this.getActiveGames().get(game).removeIf(player -> player.getProfile().getUUID().toString().equalsIgnoreCase(uuid));

        //Remove if no players are in the game.
        this.getActiveGames().values().removeIf(List::isEmpty);
    }

    public TriviaGame findGame(String code) {
        return this.getActiveGames().keySet().stream().filter(players -> players.getCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);
    }

    public Map<TriviaGame, List<Player>> getActiveGames() {
        return activeGames;
    }
    public QuestionRandomizer getQuestionRandomizer() {
        return questionRandomizer;
    }
}

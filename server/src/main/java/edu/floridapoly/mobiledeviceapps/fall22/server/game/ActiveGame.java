package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class ActiveGame {

    private final TriviaGame game;
    private final List<Player> players;
    private final QuestionRandomizer questionRandomizer;

    public ActiveGame(TriviaGame game) {
        this.game = game;
        this.players = new ArrayList<>();
        this.questionRandomizer = new QuestionRandomizer();
    }

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
    }

    public void removePlayer(String uuid) {
        this.getPlayers().removeIf(player -> player.getProfile().getUUID().toString().equalsIgnoreCase(uuid));
    }

    public QuestionRandomizer getQuestionRandomizer() {
        return questionRandomizer;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public TriviaGame getGame() {
        return game;
    }
}

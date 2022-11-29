package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class ActiveGame {

    private final TriviaGame game;
    private final Map<Player, WebSocket> players;
    private final QuestionRandomizer questionRandomizer;

    public ActiveGame(TriviaGame game) {
        this.game = game;
        this.players = new HashMap<>();
        this.questionRandomizer = new QuestionRandomizer();
    }

    public void addPlayer(Player player, WebSocket socket) {
        this.getPlayers().put(player, socket);
    }

    public void removePlayer(String uuid) {
        this.getPlayers().entrySet().removeIf(entry -> entry.getKey().getProfile().getUUID().toString().equalsIgnoreCase(uuid));
    }

    public QuestionRandomizer getQuestionRandomizer() {
        return questionRandomizer;
    }
    public TriviaGame getGame() {
        return game;
    }
    public Map<Player, WebSocket> getPlayers() {
        return players;
    }
}

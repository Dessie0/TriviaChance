package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class ActiveGame {

    private final TriviaGame game;
    private final Map<Player, Socket> players;
    private final QuestionRandomizer questionRandomizer;

    public ActiveGame(TriviaGame game) {
        this.game = game;
        this.players = new HashMap<>();
        this.questionRandomizer = new QuestionRandomizer();
    }

    public void addPlayer(Player player, Socket socket) {
        this.getPlayers().put(player, socket);
    }

    public void removePlayer(String uuid) {
        this.getPlayers().entrySet().removeIf(entry -> {
            if(entry.getKey().getProfile().getUUID().toString().equalsIgnoreCase(uuid)) {
                try {
                    entry.getValue().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }


    public QuestionRandomizer getQuestionRandomizer() {
        return questionRandomizer;
    }
    public TriviaGame getGame() {
        return game;
    }
    public Map<Player, Socket> getPlayers() {
        return players;
    }
}

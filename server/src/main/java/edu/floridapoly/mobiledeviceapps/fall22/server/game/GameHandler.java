package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class GameHandler {

    private final List<ActiveGame> activeGames;

    public GameHandler() {
        this.activeGames = new ArrayList<>();
    }

    public ActiveGame startGame(TriviaGame game) {
        ActiveGame activeGame = new ActiveGame(game);
        this.getActiveGames().add(new ActiveGame(game));

        return activeGame;
    }

    public void stopGame(ActiveGame game) {
        this.getActiveGames().remove(game);
    }

    public ActiveGame findGame(String code) {
        return this.getActiveGames().stream().filter(game -> game.getGame().getCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);
    }

    public ActiveGame findGame(UUID gameUUID) {
        return this.getActiveGames().stream().filter(game -> game.getGame().getUUID().equals(gameUUID))
                .findFirst().orElse(null);
    }

    public List<ActiveGame> getActiveGames() {
        return activeGames;
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.socket.TriviaChanceWebSocket;

public class GameHandler {

    private final List<ActiveGame> activeGames;
    private final TriviaChanceServer server;
    private TriviaChanceWebSocket webSocket;

    public GameHandler(TriviaChanceServer server) {
        this.activeGames = new ArrayList<>();
        this.server = server;

        new Thread(() -> {
            System.out.println("Starting websocket server...");
            this.webSocket = new TriviaChanceWebSocket(this.getServer(), new InetSocketAddress(8083));
            this.webSocket.run();
        }).start();
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
    public TriviaChanceServer getServer() {
        return server;
    }
}

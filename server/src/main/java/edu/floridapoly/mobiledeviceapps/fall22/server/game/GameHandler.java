package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.socket.PlayerThread;

public class GameHandler {

    private final List<ActiveGame> activeGames;
    private final TriviaChanceServer server;
    private ServerSocket gameSocket;

    public GameHandler(TriviaChanceServer server) {
        this.activeGames = new ArrayList<>();
        this.server = server;

        try {
            this.gameSocket = new ServerSocket(8083);
            System.out.println("Game socket started on port 8083.");
            this.startGameSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void startGameSocket() {
        new Thread(() -> {
            while(true) {
                try {
                    Socket socket = this.getGameSocket().accept();
                    new PlayerThread(this.getServer(), socket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public List<ActiveGame> getActiveGames() {
        return activeGames;
    }
    public ServerSocket getGameSocket() {
        return gameSocket;
    }
    public TriviaChanceServer getServer() {
        return server;
    }
}

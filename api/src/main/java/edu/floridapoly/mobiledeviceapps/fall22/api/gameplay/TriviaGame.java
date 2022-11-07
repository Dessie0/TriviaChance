package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TriviaGame {

    private final UUID uuid;
    private final List<Player> players = new ArrayList<>();

    public TriviaGame(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }
    public void addPlayer(Player player) {
        this.getPlayers().add(player);
    }
    public List<Player> getPlayers() {
        return players;
    }
}

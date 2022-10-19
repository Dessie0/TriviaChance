package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.TriviaChanceAPI;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks.ProfileCallback;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile.Profile;

public class TriviaGame {

    private final TriviaChanceAPI api;
    private final UUID uuid;
    private final List<Player> players = new ArrayList<>();

    public TriviaGame(TriviaChanceAPI api, UUID uuid) {
        this.api = api;
        this.uuid = uuid;
    }

    public TriviaChanceAPI getAPI() {
        return api;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }
}

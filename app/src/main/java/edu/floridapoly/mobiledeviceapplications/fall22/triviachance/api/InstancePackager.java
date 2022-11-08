package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class InstancePackager {
    private final TriviaChanceAPI api;
    private SharedPreferences preferences;
    private Profile localProfile;

    public InstancePackager(TriviaChanceAPI api, SharedPreferences preferences) {
        this.preferences = preferences;
        this.api = api;

        if(this.getPreferences().contains("profileUUID")) {
            this.setLocalProfile(UUID.fromString(this.getPreferences().getString("profileUUID", null)));
        } else {
            UUID uuid = UUID.randomUUID();
            this.getAPI().registerProfile(new Profile(uuid, Profile.generateRandomUsername(), null, new ArrayList<>()))
                    .thenAccept((saved) -> {
                        this.getPreferences().edit().putString("profileUUID", uuid.toString()).apply();
                        this.setLocalProfile(uuid);
                    });
        }
    }

    private void setLocalProfile(UUID uuid) {
        this.getAPI().retrieveProfile(uuid).thenAccept(profile -> {
            this.localProfile = profile;
        });
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
    public TriviaChanceAPI getAPI() {
        return this.api;
    }
    public Profile getLocalProfile() {
        return localProfile;
    }

}

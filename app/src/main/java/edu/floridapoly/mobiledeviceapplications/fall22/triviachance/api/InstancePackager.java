package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.MainMenu;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.R;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;


public class InstancePackager {
    private final TriviaChanceAPI api;
    private SharedPreferences preferences;
    private Profile localProfile;
    private Bitmap profileIcon;

    public InstancePackager(MainMenu mainMenu, TriviaChanceAPI api, SharedPreferences preferences) {
        this.preferences = preferences;
        this.api = api;

        if(this.getPreferences().contains("profileUUID")) {
            this.setLocalProfile(UUID.fromString(this.getPreferences().getString("profileUUID", null)))
                    .thenRun(mainMenu::onReady);
        } else {
            UUID uuid = UUID.randomUUID();
            this.getAPI().registerProfile(new Profile(uuid, Profile.generateRandomUsername(), null, new ArrayList<>()))
                    .thenAccept((saved) -> {
                        this.getPreferences().edit().putString("profileUUID", uuid.toString())
                                .putInt("ctheme", R.style.Theme_TriviaChance)
                                .putFloat("soundVolume", 0.5f)
                                .putFloat("musicVolume", 0.5f)
                                .putBoolean("notifications", true)
                                .putBoolean("vibration", true)
                                .putInt("PROGRESS", 0)
                                .putInt("UNLOCKS", 0)
                                .apply();

                        this.setLocalProfile(uuid).thenRun(mainMenu::onReady);
                    });
        }
    }

    private CompletableFuture<Void> setLocalProfile(UUID uuid) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        this.getAPI().retrieveProfile(uuid).thenAccept(profile -> {
            this.localProfile = profile;
            future.complete(null);
        });

        return future;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }
    public void setProfileIcon(Bitmap profileIcon) {
        this.profileIcon = profileIcon;
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
    public Bitmap getProfileIcon() {
        return profileIcon;
    }
}

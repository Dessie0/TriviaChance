package edu.floridapoly.mobiledeviceapps.fall22.api.profile;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;

public class Profile {

    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

    private final UUID uuid;
    private final List<item> inventory;

    private String username;

    public Profile(UUID uuid, String username, List<item> inventory) {
        this.uuid = uuid;
        this.username = username;
        this.inventory = inventory;
    }

    public UUID getUUID() {
        return uuid;
    }
    public String getUsername() {
        return username;
    }
    public List<item> getInventory() {
        return inventory;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public static String generateRandomUsername() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder("user_");

        for(int i = 0; i < 10; i++) {
            builder.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "Profile{" +
                "uuid=" + uuid +
                ", inventory=" + inventory +
                ", username='" + username + '\'' +
                '}';
    }
}

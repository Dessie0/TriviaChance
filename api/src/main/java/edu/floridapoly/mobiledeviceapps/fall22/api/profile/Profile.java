package edu.floridapoly.mobiledeviceapps.fall22.api.profile;

import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.utils.StringGenerator;

public class Profile {

    private final UUID uuid;
    private String username;
    private String iconURL;
    private final List<item> inventory;

    public Profile(UUID uuid, String username, String iconURL, List<item> inventory) {
        this.uuid = uuid;
        this.username = username;
        this.iconURL = iconURL;
        this.inventory = inventory;
    }

    public UUID getUUID() {
        return uuid;
    }
    public String getUsername() {
        return username;
    }
    public String getIconURL() {
        return iconURL;
    }
    public List<item> getInventory() {
        return inventory;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public static String generateRandomUsername() {
        return "user_" + new StringGenerator(10).generate();
    }

    @Override
    public String toString() {
        return "Profile{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", inventory=" + inventory +
                '}';
    }
}

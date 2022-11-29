package edu.floridapoly.mobiledeviceapps.fall22.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.game.GameHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.PingHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game.HostGameHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game.JoinGameHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game.LeaveGameHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.game.QuestionRetrieveHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.image.ImageDownloadHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.image.ImageUploadHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile.ProfileRegisterHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile.ProfileRetrieveHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile.ProfileUpdateIconHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile.ProfileUpdateItemHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.profile.ProfileUpdateUsernameHandler;
import me.dessie.dessielib.storageapi.CoreStorageAPI;
import me.dessie.dessielib.storageapi.container.ArrayContainer;
import me.dessie.dessielib.storageapi.decomposition.StorageDecomposer;
import me.dessie.dessielib.storageapi.format.flatfile.JSONContainer;

public class TriviaChanceServer {

    private final CoreStorageAPI storageAPI;
    private final JSONContainer profileContainer;
    private final GameHandler gameHandler;

    private TriviaChanceServer() {
        this.storageAPI = CoreStorageAPI.register();
        this.profileContainer = new JSONContainer(this.getStorageAPI(), new File("data/", "profiles.json"));
        this.gameHandler = new GameHandler(this);

        this.createServer();
        this.generateDecompose();
    }

    private void createServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
            server.createContext("/ping", new PingHandler(this));
            server.createContext("/profile", new ProfileRetrieveHandler(this));
            server.createContext("/profile/register", new ProfileRegisterHandler(this));
            server.createContext("/profile/update/username", new ProfileUpdateUsernameHandler(this));
            server.createContext("/profile/update/icon", new ProfileUpdateIconHandler(this));
            server.createContext("/profile/update/item", new ProfileUpdateItemHandler(this));
            server.createContext("/image/upload", new ImageUploadHandler(this));
            server.createContext("/image/download", new ImageDownloadHandler(this));
            server.createContext("/game/host", new HostGameHandler(this));
            server.createContext("/game/join", new JoinGameHandler(this));
            server.createContext("/game/leave", new LeaveGameHandler(this));
            server.createContext("/game/question", new QuestionRetrieveHandler(this));

            System.out.println("Server started on port " + server.getAddress().getPort());

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDecompose() {
        //this.getStorageAPI().addStorageEnum(item.rarity.class);

        this.getStorageAPI().addStorageDecomposer(new StorageDecomposer<>(Profile.class, (profile, decomposedObject) -> {
            decomposedObject.addDecomposedKey("uuid", profile.getUUID().toString());
            decomposedObject.addDecomposedKey("username", profile.getUsername());
            decomposedObject.addDecomposedKey("inventory", profile.getInventory());
            decomposedObject.addDecomposedKey("iconURL", profile.getIconURL());
            decomposedObject.addDecomposedKey("numberOfUnlocks", profile.getNumberOfUnlocks());

            return decomposedObject;
        }, (container, recomposer) -> {
            recomposer.addRecomposeKey("uuid", String.class, container::retrieveAsync);
            recomposer.addRecomposeKey("username", String.class, container::retrieveAsync);
            recomposer.addRecomposeKey("iconURL", String.class, container::retrieveAsync);
            recomposer.addRecomposeKey("numberOfUnlocks", Integer.class, container::retrieveAsync);
            recomposer.addRecomposeKey("inventory", List.class, (path) -> {
                if(container instanceof ArrayContainer arrayContainer) {
                    return arrayContainer.retrieveListAsync(item.class, path);
                }
                return null;
            });

            return recomposer.onComplete((completed) -> {
                String uuid = completed.getCompletedObject("uuid");
                String username = completed.getCompletedObject("username");
                String iconURL = completed.getCompletedObject("iconURL");
                List<item> inventory = completed.getCompletedObject("inventory");
                Integer unlocks = completed.getCompletedObject("numberOfUnlocks");

                return new Profile(UUID.fromString(uuid), username, iconURL, inventory, unlocks);
            });
        }));

        this.getStorageAPI().addStorageDecomposer(new StorageDecomposer<>(item.class, (item, decomposedObject) -> {
            decomposedObject.addDecomposedKey("id", item.getItemID());
            decomposedObject.addDecomposedKey("quantity", item.getQuantity());

            return decomposedObject;
        }, (container, recomposer) -> {
            recomposer.addRecomposeKey("id", Integer.class, container::retrieveAsync);
            recomposer.addRecomposeKey("quantity", Integer.class, container::retrieveAsync);

            return recomposer.onComplete((completed) -> {
                Integer id = completed.getCompletedObject("id");
                Integer quantity = completed.getCompletedObject("quantity");

                return new item(id, quantity);
            });
        }));
    }

    public static void main(String[] args) {
        new TriviaChanceServer();
    }

    public JSONContainer getProfileContainer() {
        return profileContainer;
    }
    public CoreStorageAPI getStorageAPI() {
        return storageAPI;
    }
    public GameHandler getGameHandler() {
        return gameHandler;
    }
}
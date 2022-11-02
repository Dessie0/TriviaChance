package edu.floridapoly.mobiledeviceapps.fall22.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.ProfileRegisterHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.ProfileRetrieveHandler;
import me.dessie.dessielib.storageapi.CoreStorageAPI;
import me.dessie.dessielib.storageapi.container.ArrayContainer;
import me.dessie.dessielib.storageapi.decomposition.StorageDecomposer;
import me.dessie.dessielib.storageapi.format.flatfile.JSONContainer;

public class TriviaChanceServer {

    private final CoreStorageAPI storageAPI;
    private final JSONContainer profileContainer;

    private TriviaChanceServer() {
        this.storageAPI = CoreStorageAPI.register();
        this.profileContainer = new JSONContainer(this.getStorageAPI(), new File("data/", "profiles.json"));

        this.createServer();
        this.generateDecompose();
    }

    private void createServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/profile", new ProfileRetrieveHandler(this));
            server.createContext("/profile/register", new ProfileRegisterHandler(this));

            System.out.println("Server started on port " + server.getAddress().getPort());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDecompose() {
        this.getStorageAPI().addStorageEnum(item.rarity.class);

        this.getStorageAPI().addStorageDecomposer(new StorageDecomposer<>(Profile.class, (profile, decomposedObject) -> {
            decomposedObject.addDecomposedKey("uuid", profile.getUUID());
            decomposedObject.addDecomposedKey("username", profile.getUsername());
            decomposedObject.addDecomposedKey("inventory", profile.getInventory());

            return decomposedObject;
        }, (container, recomposer) -> {
            recomposer.addRecomposeKey("uuid", String.class, container::retrieveAsync);
            recomposer.addRecomposeKey("username", String.class, container::retrieveAsync);
            recomposer.addRecomposeKey("inventory", List.class, (path) -> {
                if(container instanceof ArrayContainer arrayContainer) {
                    return arrayContainer.retrieveListAsync(item.class, path);
                }
                return null;
            });

            return recomposer.onComplete((completed) -> {
                String uuid = completed.getCompletedObject("uuid");
                String username = completed.getCompletedObject("username");
                List<item> inventory = completed.getCompletedObject("inventory");

                return new Profile(UUID.fromString(uuid), username, inventory);
            });
        }));

        this.getStorageAPI().addStorageDecomposer(new StorageDecomposer<>(item.class, (item, decomposedObject) -> {
            decomposedObject.addDecomposedKey("id", item.itemID);
            decomposedObject.addDecomposedKey("quantity", item.quantity);

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
}
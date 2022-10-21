package edu.floridapoly.mobiledeviceapps.fall22.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.ProfileRegisterHandler;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.ProfileRetrieveHandler;
import me.dessie.dessielib.storageapi.CoreStorageAPI;
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

            System.out.println("Server started.");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDecompose() {
        this.getStorageAPI().addStorageDecomposer(new StorageDecomposer<>(Profile.class, ((profile, decomposedObject) -> {
            decomposedObject.addDecomposedKey("uuid", profile.getUUID());
            decomposedObject.addDecomposedKey("username", profile.getUsername());

            return decomposedObject;
        }), ((container, recomposer) -> {
            recomposer.addRecomposeKey("uuid", String.class, container::retrieveAsync);
            recomposer.addRecomposeKey("username", String.class, container::retrieveAsync);

            return recomposer.onComplete((completed) -> {
               String uuid = completed.getCompletedObject("uuid");
               String username = completed.getCompletedObject("username");

               return new Profile(UUID.fromString(uuid), username);
            });
        })));
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
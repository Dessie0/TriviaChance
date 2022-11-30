package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks.FutureCallback;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.GameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.util.EventHandler;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.util.TriviaChanceListener;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.TextQuestion;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaChanceAPI {

    //Timeout to retrieve requests, in Milliseconds.
    private static final int TIMEOUT = 10000;
    private static final int MAX_ICON_HEIGHT = 512;
    private static final int MAX_ICON_WIDTH = 512;

    //static final String SERVER_IP = "192.168.1.172";
    static final String SERVER_IP = "51.79.52.211";

    private final TriviaChanceService service;
    private final TriviaSocketInterface socketInterface;

    private final List<TriviaChanceListener> listeners = new ArrayList<>();

    //Holds if the player is in a game or not, null if they are not playing a game.
    private TriviaGame currentGame;

    public TriviaChanceAPI() {
        Retrofit connection = new Retrofit.Builder()
                .baseUrl("http://" + SERVER_IP + ":8082/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = connection.create(TriviaChanceService.class);
        this.socketInterface = new TriviaSocketInterface(this);
    }

    public CompletableFuture<Boolean> ping() {
        Call<Boolean> call = this.getService().ping();
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Profile> retrieveProfile(UUID uuid) {
        Call<Profile> call = this.getService().retrieveProfile(uuid.toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Boolean> registerProfile(Profile profile) {
        Call<Boolean> call = this.getService().registerProfile(profile.getUUID().toString(), profile.getUsername());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Profile> updateUsername(Profile profile, String username) {
        Call<Profile> call = this.getService().updateUsername(profile.getUUID().toString(), username);
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Profile> updateIcon(Profile profile, String iconURL) {
        Call<Profile> call = this.getService().updateIcon(profile.getUUID().toString(), iconURL);
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Profile> updateItem(Profile profile, int itemId, int quantity) {
        Call<Profile> call = this.getService().updateItem(profile.getUUID().toString(), itemId, quantity);
        return this.enqueue(call, new FutureCallback<>());
    }

    /*public CompletableFuture<Profile> addToInventory(Profile profile, item Item){
        Call<Profile> call = this.getService().addToInventory(profile.getUUID(), Item);
        return this.enqueue(call, new FutureCallback<>());
    }*/

    public CompletableFuture<List<Player>> retrieveGameLeaderboard(TriviaGame game) {
        Call<List<Player>> call = this.getService().retrieveGameLeaderboard(game.getUUID().toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<TriviaGame> createGame(Profile host) {
        Call<TriviaGame> call = this.getService().createGame();
        return this.enqueue(call, new FutureCallback<>()).whenComplete((game, err) -> {
            if(game == null) return;

            this.getSocketInterface().joinGame(host, game);
        });
    }

    public CompletableFuture<TriviaGame> joinGame(Profile player, String code) {
        Call<TriviaGame> call = this.getService().joinGame(code);
        return this.enqueue(call, new FutureCallback<>()).whenComplete((game, err) -> {
            if(game == null) return;

            this.getSocketInterface().joinGame(player, game);
        });
    }

    public CompletableFuture<Boolean> leaveGame(Profile player, TriviaGame game) {
        Call<Boolean> call = this.getService().leaveGame(player.getUUID().toString(), game.getUUID().toString());

        return this.enqueue(call, new FutureCallback<>()).whenComplete((left, err) -> {
            this.getSocketInterface().leaveGame(player, game);
        });
    }

    public CompletableFuture<Boolean> kickPlayer(Profile player, TriviaGame game) {
        Call<Boolean> call = this.getService().kickPlayer(player.getUUID().toString(), game.getUUID().toString());

        return this.enqueue(call, new FutureCallback<>()).whenComplete((left, err) -> {
            this.getSocketInterface().kickPlayer(player, game);
        });
    }

    public CompletableFuture<Question<?>> retrieveQuestion(TriviaGame game, int index) {

        //TODO Add more questions besides just text questions
        CompletableFuture<Question<?>> future = new CompletableFuture<>();
        this.retrieveTextQuestion(game, index).thenAccept(future::complete);
        return future;
    }

    public CompletableFuture<String> uploadImage(Bitmap bitmap) {
        if(bitmap.getHeight() > MAX_ICON_HEIGHT || bitmap.getWidth() > MAX_ICON_WIDTH) {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalStateException("Image too large, max size is " + MAX_ICON_WIDTH + "x" + MAX_ICON_HEIGHT + "."));
            return future;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] bytes = out.toByteArray();

        Call<ResponseBody> call = this.getService().uploadImage(Base64.getEncoder().encodeToString(bytes));
        CompletableFuture<String> future = new CompletableFuture<>();
        this.enqueue(call, new FutureCallback<>()).thenAccept((responseBody) -> {
            try {
                future.complete(responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return future;
    }

    private CompletableFuture<TextQuestion> retrieveTextQuestion(TriviaGame game, int index) {
        Call<TextQuestion> call = this.getService().retrieveTextQuestion(game.getUUID().toString(), index);
        return this.enqueue(call, new FutureCallback<>());
    }

    private <T> CompletableFuture<T> enqueue(Call<T> call, FutureCallback<T> callback) {
        call.enqueue(callback);
        call.timeout().deadline(TIMEOUT, TimeUnit.MILLISECONDS);

        return callback.getFuture();
    }

    public void setCurrentGame(TriviaGame currentGame) {
        this.currentGame = currentGame;
    }

    public TriviaSocketInterface getSocketInterface() {
        return socketInterface;
    }
    public TriviaGame getCurrentGame() {
        return currentGame;
    }
    public TriviaChanceService getService() {
        return service;
    }

    void fireEvent(GameEvent event) {
        for(TriviaChanceListener listener : this.listeners) {
            Class<?> clazz = listener.getClass();
            for(Method method : clazz.getDeclaredMethods()) {
                if(!method.isAnnotationPresent(EventHandler.class)) continue;
                if(method.getParameterCount() != 1) continue;
                if(method.getParameterTypes()[0] != event.getClass()) continue;

                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void registerListener(TriviaChanceListener listener) {
        this.listeners.add(listener);
    }
    public void unregisterListener(TriviaChanceListener listener) {
        this.listeners.remove(listener);
    }
}

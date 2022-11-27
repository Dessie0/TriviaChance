package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks.FutureCallback;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.item;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.TextQuestion;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.MessageType;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.SocketMessageGenerator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaChanceAPI {

    //Timeout to retrieve requests, in Milliseconds.
    private static final int TIMEOUT = 10000;
    private static final int MAX_ICON_HEIGHT = 512;
    private static final int MAX_ICON_WIDTH = 512;

    //private static final String SERVER_IP = "192.168.1.87";
    private static final String SERVER_IP = "51.79.52.211";

    private final TriviaChanceService service;
    private final WebSocket socket;

    public TriviaChanceAPI() {
        Retrofit connection = new Retrofit.Builder()
                .baseUrl("http://" + SERVER_IP + ":8082/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://" + SERVER_IP + ":8083/")
                .build();

        this.socket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("Closed because: " + reason);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                System.out.println("Closing because: " + reason);
            }

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("Opened.");
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);

                System.out.println("Received message: " + text);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
                super.onFailure(webSocket, t, response);
            }
        });

        this.service = connection.create(TriviaChanceService.class);
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
        //TODO Connect to server socket.

        System.out.println(this.getSocket());

        System.out.println("Hosting game...");
        return this.enqueue(call, new FutureCallback<>()).whenComplete((game, err) -> {
            System.out.println(game);
            if(game == null) return;

            //TODO Connect to server socket.
            SocketMessageGenerator generator = new SocketMessageGenerator(MessageType.JOIN_GAME);
            generator.setParam("profileUUID", host.getUUID().toString());
            generator.setParam("gameUUID", game.getUUID().toString());

            System.out.println("Sending " + generator.generate());
            this.getSocket().send(generator.generate());
        });
    }

    public CompletableFuture<TriviaGame> joinGame(Profile member, String code) {
        Call<TriviaGame> call = this.getService().joinGame(code);
        return this.enqueue(call, new FutureCallback<>()).whenComplete((game, err) -> {
            if(game == null) return;

            //TODO Connect to server socket.
            SocketMessageGenerator generator = new SocketMessageGenerator(MessageType.JOIN_GAME);
            generator.setParam("profileUUID", member.getUUID().toString());
            generator.setParam("gameUUID", game.getUUID().toString());

            this.getSocket().send(generator.generate());
        });
    }

    public CompletableFuture<Boolean> leaveGame(Profile member, TriviaGame game) {
        Call<Boolean> call = this.getService().leaveGame(member.getUUID().toString(), game.getUUID().toString());
        //TODO Disconnect from server socket.

        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Question<?>> retrieveQuestion(TriviaGame game) {

        //TODO Add more questions besides just text questions
        CompletableFuture<Question<?>> future = new CompletableFuture<>();
        this.retrieveTextQuestion(game).thenAccept(future::complete);
        return future;
    }

    public CompletableFuture<String> uploadImage(Bitmap bitmap) {
        if(bitmap.getHeight() > MAX_ICON_HEIGHT || bitmap.getWidth() > MAX_ICON_WIDTH) {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalStateException("Image too large, max size is 256x256."));
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

    private CompletableFuture<TextQuestion> retrieveTextQuestion(TriviaGame game) {
        Call<TextQuestion> call = this.getService().retrieveTextQuestion(game.getUUID().toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    private <T> CompletableFuture<T> enqueue(Call<T> call, FutureCallback<T> callback) {
        call.enqueue(callback);
        call.timeout().deadline(TIMEOUT, TimeUnit.MILLISECONDS);

        return callback.getFuture();
    }

    public WebSocket getSocket() {
        return socket;
    }
    public TriviaChanceService getService() {
        return service;
    }
}

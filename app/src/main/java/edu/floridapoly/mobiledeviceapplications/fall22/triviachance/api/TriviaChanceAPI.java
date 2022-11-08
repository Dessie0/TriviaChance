package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks.FutureCallback;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaChanceAPI {

    //Timeout to retrieve requests, in Milliseconds.
    private static final int TIMEOUT = 10000;
    private static final String LOCAL_IP = "51.79.52.211";

    private final Retrofit connection;
    private final TriviaChanceService service;

    public TriviaChanceAPI() {
        this.connection = new Retrofit.Builder()
                .baseUrl("http://" + LOCAL_IP + ":8082/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = this.connection.create(TriviaChanceService.class);
    }

    public CompletableFuture<Profile> retrieveProfile(UUID uuid) {
        Call<Profile> call = this.getService().retrieveProfile(uuid.toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Boolean> registerProfile(Profile profile) {
        Call<Boolean> call = this.getService().registerProfile(profile.getUUID().toString(), profile.getUsername());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<List<Player>> retrieveGameLeaderboard(TriviaGame game) {
        Call<List<Player>> call = this.getService().retrieveGameLeaderboard(game.getUUID().toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<TriviaGame> createGame(Profile host) {
        Call<TriviaGame> call = this.getService().createGame(host.getUUID().toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<TriviaGame> joinGame(Profile member, String code) {
        Call<TriviaGame> call = this.getService().joinGame(member.getUUID().toString(), code);
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Boolean> leaveGame(Profile member, TriviaGame game) {
        Call<Boolean> call = this.getService().leaveGame(member.getUUID().toString(), game.getUUID().toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    public CompletableFuture<Question<?>> retrieveQuestion(TriviaGame game) {
        Call<Question<?>> call = this.getService().retrieveQuestion(game.getUUID().toString());
        return this.enqueue(call, new FutureCallback<>());
    }

    private <T> CompletableFuture<T> enqueue(Call<T> call, FutureCallback<T> callback) {
        call.enqueue(callback);
        call.timeout().deadline(TIMEOUT, TimeUnit.MILLISECONDS);

        return callback.getFuture();
    }

    public TriviaChanceService getService() {
        return service;
    }
}

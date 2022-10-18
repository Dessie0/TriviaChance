package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks.ProfileCallback;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile.Profile;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaChanceAPI {

    //Timeout to retrieve requests, in Milliseconds.
    private static final int TIMEOUT = 10000;
    private static final String LOCAL_IP = "10.221.86.113";

    private final Retrofit connection;
    private final TriviaChanceService service;

    public TriviaChanceAPI() {
        this.connection = new Retrofit.Builder()
                .baseUrl("http://" + LOCAL_IP + ":8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = this.connection.create(TriviaChanceService.class);
    }

    public CompletableFuture<Profile> retrieveProfile(UUID uuid) {
        Call<Profile> profileCall = this.getService().retrieveProfile(uuid.toString());
        ProfileCallback callback = new ProfileCallback();
        profileCall.enqueue(callback);
        profileCall.timeout().deadline(TIMEOUT, TimeUnit.MILLISECONDS);

        return callback.getFuture();
    }

    public TriviaChanceService getService() {
        return service;
    }
}

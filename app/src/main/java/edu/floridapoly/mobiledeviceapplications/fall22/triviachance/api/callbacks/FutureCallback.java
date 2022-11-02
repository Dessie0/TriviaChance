package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks;

import java.util.concurrent.CompletableFuture;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FutureCallback<T> implements Callback<T> {
    private final CompletableFuture<T> future = new CompletableFuture<>();

    public CompletableFuture<T> getFuture() {
        return future;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()) {
            this.getFuture().complete(response.body());
        } else {
            this.getFuture().completeExceptionally(new IllegalStateException("Response was unsuccessful."));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        this.getFuture().completeExceptionally(t);
    }
}

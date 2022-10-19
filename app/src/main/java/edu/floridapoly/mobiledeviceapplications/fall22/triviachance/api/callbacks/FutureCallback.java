package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class FutureCallback<T> implements Callback<T> {
    private final CompletableFuture<T> future = new CompletableFuture<>();

    public CompletableFuture<T> getFuture() {
        return future;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        this.getFuture().completeExceptionally(t);
    }
}

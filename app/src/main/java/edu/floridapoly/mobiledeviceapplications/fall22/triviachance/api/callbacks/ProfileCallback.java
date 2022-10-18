package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.callbacks;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileCallback extends FutureCallback<Profile> implements Callback<Profile> {
    @Override
    public void onResponse(Call<Profile> call, Response<Profile> response) {
        if(response.isSuccessful()) {
            Profile profile = response.body();
            this.getFuture().complete(profile);
        }
    }
}

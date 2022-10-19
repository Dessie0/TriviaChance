package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaChanceService {
    @GET("profile")
    Call<Profile> retrieveProfile(@Query("id") String id);
}

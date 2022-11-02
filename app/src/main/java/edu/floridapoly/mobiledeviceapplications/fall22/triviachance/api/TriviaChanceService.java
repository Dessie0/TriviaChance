package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaChanceService {
    @GET("profile")
    Call<Profile> retrieveProfile(@Query("uuid") String uuid);

    @GET("profile/register")
    Call<Boolean> registerProfile(@Query("uuid") String uuid,
                                  @Query("username") String username);

    @GET("game/host")
    Call<TriviaGame> createGame(@Query("uuid") String uuid);

    @GET("game/join")
    Call<TriviaGame> joinGame(@Query("uuid") String uuid,
                              @Query("code") String code);

    @GET("game/submit")
    Call<Boolean> submitQuestion(@Query("gameId") String gameId,
                                 @Query("playerUuid") String uuid,
                                 @Query("questionId") int questionId,
                                 @Query("answer") Object answer);

    @GET("game/leaderboard")
    Call<List<Player>> retrieveGameLeaderboard(@Query("gameId") String gameId);

}
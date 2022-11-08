package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaChanceService {
    @GET("profile")
    Call<Profile> retrieveProfile(@Query("profileUUID") String uuid);

    @GET("profile/register")
    Call<Boolean> registerProfile(@Query("profileUUID") String uuid,
                                  @Query("username") String username);

    @GET("game/host")
    Call<TriviaGame> createGame(@Query("profileUUID") String uuid);

    @GET("game/join")
    Call<TriviaGame> joinGame(@Query("profileUUID") String uuid,
                              @Query("code") String code);

    @GET("game/join")
    Call<Boolean> leaveGame(@Query("profileUUID") String profileUuid,
                              @Query("gameUUID") String gameUuid);

    @GET("game/question")
    Call<Question<?>> retrieveQuestion(@Query("gameUUID") String gameUuid);

    @GET("game/leaderboard")
    Call<List<Player>> retrieveGameLeaderboard(@Query("gameId") String gameId);

}
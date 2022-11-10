package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.TextQuestion;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TriviaChanceService {
    @POST("ping")
    Call<Boolean> ping();

    @GET("profile")
    Call<Profile> retrieveProfile(@Query("profileUUID") String profileUUID);

    @POST("profile/register")
    Call<Boolean> registerProfile(@Query("profileUUID") String profileUUID,
                                  @Query("username") String username);

    @POST("profile/update/username")
    Call<Profile> updateUsername(@Query("profileUUID") String profileUUID,
                                 @Query("username") String username);

    @POST("profile/update/icon")
    Call<Profile> updateIcon(@Query("profileUUID") String profileUUID,
                                 @Query("iconURL") String iconURL);

    @POST("profile/update/item")
    Call<Profile> updateItem(@Query("profileUUID") String profileUUID,
                             @Query("itemId") int itemId,
                             @Query("quantity") int quantity);

    @GET("game/host")
    Call<TriviaGame> createGame(@Query("profileUUID") String profileUUID);

    @GET("game/join")
    Call<TriviaGame> joinGame(@Query("profileUUID") String profileUUID,
                              @Query("code") String code);

    @GET("game/leave")
    Call<Boolean> leaveGame(@Query("profileUUID") String profileUuid,
                              @Query("gameUUID") String gameUuid);

    @GET("game/question?type=text")
    Call<TextQuestion> retrieveTextQuestion(@Query("gameUUID") String gameUuid);

    @GET("game/leaderboard")
    Call<List<Player>> retrieveGameLeaderboard(@Query("gameId") String gameId);

}
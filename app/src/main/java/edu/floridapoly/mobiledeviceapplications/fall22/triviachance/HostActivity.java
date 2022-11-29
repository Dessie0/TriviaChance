package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.PlayerJoinGameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.PlayerLeaveGameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.StartGameEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.util.EventHandler;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.util.TriviaChanceListener;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;

public class HostActivity extends AppCompatActivity implements TriviaChanceListener {

    TextView gameId;
    Button startGameButton;
    ImageButton backButton;
    RecyclerView playerList;
    PlayerGalleryAdapter adapter;
    ArrayList<Player> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_host);

        TriviaGame game = MainMenu.getAPI().getCurrentGame();

        gameId = findViewById(R.id.gameID);
        gameId.setText(game.getCode());

        startGameButton = findViewById(R.id.startGame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenu.getAPI().getSocketInterface().startGame(game);
            }
        });

        backButton = findViewById(R.id.hostBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HostActivity.this, MainMenu.class);
                startActivity(intent);

                MainMenu.getAPI().leaveGame(MainMenu.getLocalProfile(), game);
                MainMenu.getAPI().unregisterListener(HostActivity.this);
            }
        });

        this.adapter = new PlayerGalleryAdapter(list, getApplication());

        playerList = findViewById(R.id.playerRecView);
        playerList.setAdapter(adapter);
        playerList.setLayoutManager(new LinearLayoutManager(HostActivity.this));

        MainMenu.getAPI().registerListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MainMenu.getAPI().unregisterListener(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinGameEvent event) {
        list.add(new Player(event.getProfile()));
        this.adapter.notifyItemInserted(list.size() - 1);
    }

    @EventHandler
    public void onPlayerLeave(PlayerLeaveGameEvent event) {
        int index = -1;
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getProfile().getUUID().equals(event.getProfile().getUUID())) {
                index = i;
                break;
            }
        }

        if(index == -1) return;

        list.removeIf(player -> player.getProfile().getUUID().toString().equals(event.getProfile().getUUID().toString()));
        this.adapter.notifyItemRemoved(index);
    }

    @EventHandler
    public void onGameStart(StartGameEvent event) {
        Intent intent = new Intent(HostActivity.this, QuestionActivity.class);
        startActivity(intent);
    }
}
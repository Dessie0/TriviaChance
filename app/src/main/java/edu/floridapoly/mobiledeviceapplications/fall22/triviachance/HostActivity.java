package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;
import edu.floridapoly.mobiledeviceapps.fall22.api.utils.StringGenerator;

public class HostActivity extends AppCompatActivity {

    TextView gameId;
    Button startGameButton;
    ImageButton backButton;
    RecyclerView playerList;
    ArrayList<Player> list = new ArrayList<Player>(Collections.emptyList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_host);

        TriviaGame game = (TriviaGame) this.getIntent().getSerializableExtra("triviagame");

        // for testing purposes only
        for (int i = 0; i < 8; i++) {
            list.add(new Player(new Profile(UUID.randomUUID(), Profile.generateRandomUsername(), MainMenu.getLocalProfile().getIconURL(), new ArrayList<>())));
        }

        gameId = findViewById(R.id.gameID);
        gameId.setText(game.getCode());

        startGameButton = findViewById(R.id.startGame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HostActivity.this, QuestionActivity.class);
                intent.putExtra("triviagame", game);
                startActivity(intent);
            }
        });

        backButton = findViewById(R.id.hostBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HostActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

        playerList = findViewById(R.id.playerRecView);
        PlayerGalleryAdapter adapter = new PlayerGalleryAdapter(list, getApplication());
        playerList.setAdapter(adapter);
        playerList.setLayoutManager(new LinearLayoutManager(HostActivity.this));

    }
}
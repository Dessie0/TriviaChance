package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


public class HostActivity extends AppCompatActivity {

    TextView gameId;
    Button startGameButton;
    ImageButton backButton;
    RecyclerView playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_host);


        gameId = findViewById(R.id.gameID);
        gameId.setText("Usdf3wdwfe4efe");

        startGameButton = findViewById(R.id.startGame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HostActivity.this, QuestionActivity.class);
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

    }
}
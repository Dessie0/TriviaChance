package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.TriviaChanceAPI;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.gameplay.Player;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.profile.Profile;

public class MainMenu extends AppCompatActivity {

    private TriviaChanceAPI api;
    private Profile localProfile;

    Button playOnline;
    Button playSolo;
    Button hostGame;
    EditText joinGame;
    ImageButton back;
    ImageButton settings;
    ImageButton inventory;
    ImageButton editIcon;
    ImageView playerIcon;

    /*
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            }
    );
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Create the API.
        this.api = new TriviaChanceAPI();

        //TODO Make this use a Persistent UUID instead of a random one.
        this.getAPI().retrieveProfile(UUID.randomUUID()).thenAccept(profile -> {
            this.localProfile = profile;
            System.out.println("Local profile set to " + this.getLocalProfile());
        }).exceptionally(err -> {
            Toast.makeText(getBaseContext(), "Unable to connect to server.", Toast.LENGTH_LONG).show();
            return null;
        });

        playOnline = findViewById(R.id.play_online);
        playOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainMenu.this, QuestionActivity.class);
                startActivity(intent);
            }
        });
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        hostGame = findViewById(R.id.hostButton);
        hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        joinGame = findViewById(R.id.joinGame);
        joinGame.setHintTextColor(getResources().getColor(R.color.dark_blue));


        playSolo = findViewById(R.id.play_solo);
        playSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the game
                TriviaGame game = new TriviaGame(MainMenu.this.getAPI(), UUID.randomUUID());
                game.addPlayer(new Player(MainMenu.this.getLocalProfile()));

                Intent intent = new Intent(MainMenu.this, QuestionActivity.class);
                startActivity(intent);
            }
        });

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        inventory = findViewById(R.id.inventory);
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, InventoryActivity.class);
                // taken out temporarily
               // activityResultLauncher.launch(intent);
                startActivity(intent);
            }
        });

        playerIcon = findViewById(R.id.playerIcon);
        editIcon = findViewById(R.id.editIconButton);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Opening Gallery", Toast.LENGTH_SHORT).show();
                // started Idea to open gallery
                //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult();
            }
        });


    }

    public TriviaChanceAPI getAPI() {
        return this.api;
    }

    public Profile getLocalProfile() {
        return localProfile;
    }
}
package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.TriviaChanceAPI;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class MainMenu extends AppCompatActivity {

    private SharedPreferences preferences;
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
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_main_menu);

        //Get the private shared preferences for TriviaChance
        this.preferences = this.getSharedPreferences("triviachance", MODE_PRIVATE);

        //Create the API.
        this.api = new TriviaChanceAPI();

        Log.d("MainMenu", "Start setting local profile");
        if(this.getPreferences().contains("profileUUID")) {
            System.out.println("Found profileUUID in preferences.");
            this.setLocalProfile(UUID.fromString(this.getPreferences().getString("profileUUID", null)));
        } else {
            System.out.println("Generating");
            UUID uuid = UUID.randomUUID();
            System.out.println("Generated " + uuid);
            this.getAPI().registerProfile(new Profile(uuid, Profile.generateRandomUsername(), new ArrayList<>()))
                    .thenAccept((saved) -> {
                        this.getPreferences().edit().putString("profileUUID", uuid.toString()).apply();
                        this.setLocalProfile(uuid);
                    }).exceptionally(err -> {
                        err.printStackTrace();
                        Toast.makeText(getBaseContext(), "Unable to connect to server.", Toast.LENGTH_SHORT).show();
                        return null;
                    });
        }

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
                TriviaGame game = new TriviaGame(UUID.randomUUID().toString());
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
                //startActivity(intent);
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

    @Override
    public void onResume() {
        // need to change main menu with rest of the activities
        // issue is that main menu was already created with old theme so the menu needs to be reset before coming back from settings
        //super.onRestart();
        Log.d("Testing", "OnResume() called");
        super.onResume();
        ThemeUtil.onActivityCreateTheme(this);
    }

    private void setLocalProfile(UUID uuid) {
        Log.d("MainMenu", "Setting local profile");

        this.getAPI().retrieveProfile(uuid).thenAccept(profile -> {
            this.localProfile = profile;
            Log.d("[Debug]", "Local profile set to " + this.getLocalProfile());
        }).exceptionally(err -> {
            Log.e("MainMenu", "Sending err toast");
            Toast.makeText(getBaseContext(), "Unable to connect to server.", Toast.LENGTH_SHORT).show();
            Log.e("MainMenu", "Bad error", err);
            return null;
        });
    }

    public TriviaChanceAPI getAPI() {
        return this.api;
    }
    public Profile getLocalProfile() {
        return localProfile;
    }
    public SharedPreferences getPreferences() {
        return preferences;
    }
}
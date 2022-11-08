package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import java.util.ArrayList;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.TriviaChanceAPI;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;


public class MainMenu extends AppCompatActivity {

    private SharedPreferences preferences;
    private TriviaChanceAPI api;
    private Profile localProfile;

    MotionLayout layout;
    Button playOnline;
    Button playSolo;
    Button hostGame;
    EditText joinGame;
    ImageButton back;
    ImageButton settings;
    ImageButton inventory;
    ImageButton editIcon;
    ImageView playerIcon;

    int SELECT_PICTURE = 200;

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
        if(this.getPreferences().contains("profileUUID")) {
            this.setLocalProfile(UUID.fromString(this.getPreferences().getString("profileUUID", null)));
        } else {
            UUID uuid = UUID.randomUUID();
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

        joinGame = findViewById(R.id.joinGame);
        layout = findViewById(R.id.motionLayout);
        playOnline = findViewById(R.id.play_online);
        playOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.transitionToEnd();

                if (joinGame.getText().toString().length() != 0){
                    Intent intent = new Intent(MainMenu.this, QuestionActivity.class);
                    joinGame.setText("");
                    startActivity(intent);
                }
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

        playSolo = findViewById(R.id.play_solo);
        playSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the game
                if(MainMenu.this.getLocalProfile() == null) {
                    Toast.makeText(MainMenu.this, "Unable to start game, please check internet connection.", Toast.LENGTH_LONG).show();
                    return;
                }

                MainMenu.this.getAPI().createGame(MainMenu.this.getLocalProfile()).thenAccept(game -> {
                    System.out.println(game.getCode());
                });

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

                //Toast.makeText(getBaseContext(), "Opening Gallery", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
    }

    // only works per instance, still resets when app is killed
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    playerIcon.setImageURI(selectedImageUri);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //overrides phone back button to undo animation instead of killing app
        //without this, if user presses back while their in the play online screen they killed the app instead of showing main menu
        // will still kill app if user is in main menu
        if (layout.getProgress() != 0.0f) {
            layout.transitionToStart();
        }
        else {
            super.onBackPressed();
        }
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
        this.getAPI().retrieveProfile(uuid).thenAccept(profile -> {
            this.localProfile = profile;
            Log.d("MainMenu", "Local profile set to " + this.getLocalProfile());
        }).exceptionally(err -> {
            Toast.makeText(getBaseContext(), "Unable to connect to server.", Toast.LENGTH_SHORT).show();
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
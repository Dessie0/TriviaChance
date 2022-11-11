package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import java.io.IOException;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.InstancePackager;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.TriviaChanceAPI;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon.ProfileIconHelper;
import edu.floridapoly.mobiledeviceapps.fall22.api.profile.Profile;

public class MainMenu extends AppCompatActivity {

    private static InstancePackager instancePackager;

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

        playerIcon = findViewById(R.id.playerIcon);

        //Create the static instance packager.
        if(instancePackager == null) {
            TriviaChanceAPI api = new TriviaChanceAPI();
            api.ping().thenAccept(pinged -> {
                if(pinged) {
                    instancePackager = new InstancePackager(this, api, this.getBaseContext().getSharedPreferences("triviachance", Context.MODE_PRIVATE));
                }
            }).exceptionally(err -> {
                err.printStackTrace();
                this.showNoInternetToast();
                return null;
            });
        } else {
            instancePackager.setPreferences(this.getBaseContext().getSharedPreferences("triviachance", Context.MODE_PRIVATE));
            this.onReady();
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
                    return;
                }

                MainMenu.this.getAPI().createGame(MainMenu.this.getLocalProfile()).thenAccept(game -> {
                    Intent intent = new Intent(MainMenu.this, QuestionActivity.class);
                    intent.putExtra("triviagame", game);
                    startActivity(intent);
                });
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

        editIcon = findViewById(R.id.editIconButton);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
    }

    /**
     * Called when the local profile has been retrieved from the server.
     */
    public void onReady() {
        ProfileIconHelper.reloadProfileIcon(this.getLocalProfile(), playerIcon);
    }

    //Upload the chosen profile picture and use it
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK || requestCode != SELECT_PICTURE) return;

        Uri selectedImageUri = data.getData();
        if (selectedImageUri == null) {
            Toast.makeText(this.getBaseContext(), "Unable to retrieve image.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            this.getAPI().uploadImage(bitmap).thenAccept(url -> {

                //Update the server, client, and local profile of the new icon.
                this.getLocalProfile().setIconURL(url);
                this.getAPI().updateIcon(this.getLocalProfile(), url);
                getInstancePackager().setProfileIcon(bitmap);

                ProfileIconHelper.reloadProfileIcon(this.getLocalProfile(), playerIcon);
            }).exceptionally((err) -> {
                err.printStackTrace();
                Toast.makeText(this.getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
                return null;
            });
        } catch (IOException e) {
            e.printStackTrace();
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

    public TriviaChanceAPI getAPI() {
        if(instancePackager == null) {
            this.showNoInternetToast();
            return null;
        }

        return instancePackager.getAPI();
    }
    public Profile getLocalProfile() {
        if(instancePackager == null) {
            this.showNoInternetToast();
            return null;
        }

        return instancePackager.getLocalProfile();
    }

    private void showNoInternetToast() {
        Toast.makeText(this.getBaseContext(), "Unable to connect to server. Please check your internet connection.", Toast.LENGTH_LONG).show();
    }

    public static InstancePackager getInstancePackager() {
        return instancePackager;
    }
}
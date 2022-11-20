package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

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
    ImageView playerIcon;
    TextView usernameText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_main_menu);

        playerIcon = findViewById(R.id.playerIcon);
        usernameText = findViewById(R.id.usernameTextView);

        //Create the static instance packager.
        if(instancePackager == null) {
            TriviaChanceAPI api = new TriviaChanceAPI();
            api.ping().thenAccept(pinged -> {
                if(pinged) {
                    instancePackager = new InstancePackager(this, api, this.getBaseContext().getSharedPreferences("triviachance", Context.MODE_PRIVATE));
                }
            }).exceptionally(err -> {
                err.printStackTrace();
                showNoInternetToast(this);
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

        back = findViewById(R.id.hostBackButton);


        hostGame = findViewById(R.id.hostButton);
        hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, HostActivity.class);
                startActivity(intent);
            }
        });


        playSolo = findViewById(R.id.play_solo);
        playSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the game
                if(getLocalProfile(MainMenu.this) == null) {
                    return;
                }

                getAPI(MainMenu.this).createGame(getLocalProfile(MainMenu.this)).thenAccept(game -> {
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
                startActivity(intent);
            }
        });

        inventory = findViewById(R.id.inventory);
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, InventoryActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Called when the local profile has been retrieved from the server.
     */
    public void onReady() {
        ProfileIconHelper.reloadProfileIcon(getLocalProfile(this), playerIcon);
        usernameText.setText(getLocalProfile(this).getUsername());
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

    public static TriviaChanceAPI getAPI(Context context) {
        if(instancePackager == null) {
            showNoInternetToast(context);
            return null;
        }

        return instancePackager.getAPI();
    }
    public static Profile getLocalProfile(Context context) {
        if(instancePackager == null) {
            showNoInternetToast(context);
            return null;
        }

        return instancePackager.getLocalProfile();
    }

    private static void showNoInternetToast(Context context) {
        Toast.makeText(context, "Unable to connect to server. Please check your internet connection.", Toast.LENGTH_LONG).show();
    }

    public static InstancePackager getInstancePackager() {
        return instancePackager;
    }
}
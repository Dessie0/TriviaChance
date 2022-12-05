package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon.ProfileIconHelper;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton soundToggle;
    ToggleButton musicToggle;
    ToggleButton notificationsToggle;
    ToggleButton vibrationToggle;
    ImageButton editIcon;
    ImageView settingsPlayerIcon;
    EditText username;

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_settings);

        settingsPlayerIcon = findViewById(R.id.playerIcon);
        onReady();

        SharedPreferences preferences = MainMenu.getInstancePackager().getPreferences();

        soundToggle = findViewById(R.id.soundToggle);
        musicToggle = findViewById(R.id.musicToggle);
        notificationsToggle = findViewById(R.id.notificationToggle);
        vibrationToggle = findViewById(R.id.vibrationToggle);

        soundToggle.setChecked(preferences.getBoolean("sound", false));
        soundToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    preferences.edit().putBoolean("sound", true).apply();
                }
                else {
                    preferences.edit().putBoolean("sound", false).apply();
                }
            }
        });

        musicToggle.setChecked(preferences.getBoolean("music", false));
        musicToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    preferences.edit().putBoolean("music", true).apply();
                    if (BackgroundSoundService.mediaPlayer == null) {
                        Intent intent = new Intent(SettingsActivity.this, BackgroundSoundService.class);
                        startService(intent);
                    }
                    else
                        BackgroundSoundService.mediaPlayer.start();
                }
                else {
                    preferences.edit().putBoolean("music", false).apply();
                    BackgroundSoundService.mediaPlayer.pause();
                }
            }
        });

        notificationsToggle.setChecked(preferences.getBoolean("notifications", false));
        notificationsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    preferences.edit().putBoolean("notifications", true).apply();
                }
                else {
                    preferences.edit().putBoolean("notifications", false).apply();
                }
            }
        });

        vibrationToggle.setChecked(preferences.getBoolean("vibration", false));
        vibrationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    preferences.edit().putBoolean("vibration", true).apply();
                }
                else {
                    preferences.edit().putBoolean("vibration", false).apply();
                }
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

        username = findViewById(R.id.username);
        if (MainMenu.getLocalProfile() != null) {
            username.setText(MainMenu.getLocalProfile().getUsername());
        }
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (MainMenu.getLocalProfile() != null)
                    MainMenu.getLocalProfile().setUsername(editable.toString());
                    MainMenu.getAPI().updateUsername(MainMenu.getLocalProfile(), editable.toString());
            }
        });

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
            MainMenu.getAPI().uploadImage(bitmap).thenAccept(url -> {

                //Update the server, client, and local profile of the new icon.
                MainMenu.getLocalProfile().setIconURL(url);
                MainMenu.getAPI().updateIcon(MainMenu.getLocalProfile(), url);
                MainMenu.getInstancePackager().setProfileIcon(bitmap);
                onReady();
            }).exceptionally((err) -> {
                err.printStackTrace();
                Toast.makeText(this.getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
                return null;
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onReady() {
        ProfileIconHelper.reloadProfileIcon(MainMenu.getLocalProfile(), settingsPlayerIcon);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainMenu.class);
        startActivity(intent);
    }

}
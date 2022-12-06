package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.icon.ProfileIconHelper;

public class SettingsActivity extends AppCompatActivity {

    SeekBar seekBarMusic;
    SeekBar seekBarSound;
    ToggleButton notificationsToggle;
    ToggleButton vibrationToggle;
    ImageButton editIcon;
    ImageView settingsPlayerIcon;
    EditText username;
    AudioManager audioManager;

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_settings);

        settingsPlayerIcon = findViewById(R.id.playerIcon);
        onReady();

        SharedPreferences preferences = MainMenu.getInstancePackager().getPreferences();

        seekBarMusic = findViewById(R.id.seekBarMusic);
        seekBarSound = findViewById(R.id.seekBarSound);
        notificationsToggle = findViewById(R.id.notificationToggle);
        vibrationToggle = findViewById(R.id.vibrationToggle);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        seekBarMusic.setProgress((int) (preferences.getFloat("musicVolume", 0.5f) * 100));
        seekBarSound.setProgress((int) (preferences.getFloat("soundVolume", 0.5f) * 100));

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BackgroundSoundService.mediaPlayer.setVolume((float) progress / 100,(float) progress / 100);
                preferences.edit().putFloat("musicVolume", (float) progress / 100).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                preferences.edit().putFloat("soundVolume", (float) progress / 100).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float progress = preferences.getFloat("soundVolume", 0);
                MediaPlayer correctChime = MediaPlayer.create(SettingsActivity.this, R.raw.correct);
                correctChime.setVolume(progress, progress);
                correctChime.start();
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
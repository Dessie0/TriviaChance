package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton soundToggle;
    ToggleButton musicToggle;
    ToggleButton notificationsToggle;
    ToggleButton vibrationToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        soundToggle = findViewById(R.id.soundToggle);
        musicToggle = findViewById(R.id.musicToggle);
        notificationsToggle = findViewById(R.id.notificationToggle);
        vibrationToggle = findViewById(R.id.vibrationToggle);

    }
}
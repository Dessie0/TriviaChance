package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton soundToggle;
    ToggleButton musicToggle;
    ToggleButton notificationsToggle;
    ToggleButton vibrationToggle;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_settings);

        soundToggle = findViewById(R.id.soundToggle);
        musicToggle = findViewById(R.id.musicToggle);
        notificationsToggle = findViewById(R.id.notificationToggle);
        vibrationToggle = findViewById(R.id.vibrationToggle);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.theme_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        ThemeUtil.changeToTheme(SettingsActivity.this, R.style.Theme_TriviaChance_Gold);
                        break;
                    case 2:
                        ThemeUtil.changeToTheme(SettingsActivity.this, R.style.Theme_TriviaChance_Green);
                        break;
                    case 3:
                        ThemeUtil.changeToTheme(SettingsActivity.this, R.style.Theme_TriviaChance_Red);
                        break;
                    case 4:
                        ThemeUtil.changeToTheme(SettingsActivity.this, R.style.Theme_TriviaChance);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainMenu.class);
        startActivity(intent);
    }

}
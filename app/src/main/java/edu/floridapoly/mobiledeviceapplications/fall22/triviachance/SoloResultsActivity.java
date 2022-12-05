package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SoloResultsActivity extends AppCompatActivity {

    TextView numCorrectText;
    ImageButton homeButton;
    ImageButton replayButton;
    ProgressBar progressBar;
    Button redeemButton;

    int currentProgress = MainMenu.getInstancePackager().getPreferences().getInt("PROGRESS", 0);

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        int correct = intent.getIntExtra("CORRECT", 0);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(currentProgress);
        currentProgress += (correct * 10);
        MainMenu.getInstancePackager().getPreferences().edit().putInt("PROGRESS", currentProgress).apply();

        progressBar.setSecondaryProgress(currentProgress);
        ObjectAnimator.ofInt(progressBar, "progress", currentProgress).setDuration(700).start();


        redeemButton = findViewById(R.id.resultsRedeemButton);
        redeemButton.setVisibility(View.GONE);


        if (currentProgress >= 100) {
            currentProgress = 0;
            MainMenu.getInstancePackager().getPreferences().edit().putInt("PROGRESS", currentProgress).apply();
            redeemButton.setVisibility(View.VISIBLE);
            int currentUnlocks = MainMenu.getInstancePackager().getPreferences().getInt("UNLOCKS", 0);
            MainMenu.getInstancePackager().getPreferences().edit().putInt("UNLOCKS", currentUnlocks + 1).apply();
        }
        if (MainMenu.getInstancePackager().getPreferences().getInt("UNLOCKS", 0) != 0) {
            redeemButton.setVisibility(View.VISIBLE);
        }




        numCorrectText = findViewById(R.id.numCorrectText);
        numCorrectText.setText(String.format("Correct: %s%%", correct * 10));



        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoloResultsActivity.this, RedeemActivity.class);
                startActivity(intent);
            }
        });

        homeButton = findViewById(R.id.homeButton2);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoloResultsActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

        replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the game
                if(MainMenu.getLocalProfile() == null) {
                    return;
                }

                MainMenu.getAPI().createGame(MainMenu.getLocalProfile()).thenAccept(game -> {
                    Intent intent = new Intent(SoloResultsActivity.this, QuestionActivity.class);
                    intent.putExtra("SOLO", true);
                    startActivity(intent);

                    MainMenu.getAPI().setCurrentGame(game);
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SoloResultsActivity.this, MainMenu.class);
        startActivity(intent);
    }
}
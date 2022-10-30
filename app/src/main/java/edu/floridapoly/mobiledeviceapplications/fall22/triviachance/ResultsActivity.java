package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    TextView numCorrectText;
    ProgressBar progressBar;
    Button redeemButton;

    static int currentProgress = 80;
    public static int unlocksAvaliable = 0;

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

        progressBar.setSecondaryProgress(currentProgress);
        ObjectAnimator.ofInt(progressBar, "progress", currentProgress).setDuration(700).start();


        // need to have bar fill up like normal, wait, then start at 0 and refil whats left in a different color

        if (currentProgress >= 100) {
            currentProgress -= 100;
            unlocksAvaliable++;

           progressBar.setSecondaryProgress(currentProgress);

            ObjectAnimator.ofInt(progressBar, "progress", currentProgress).setDuration(700).start();
        }




        numCorrectText = findViewById(R.id.numCorrectText);
        numCorrectText.setText(String.format("Correct: %s%%", correct * 10));



        redeemButton = findViewById(R.id.resultsRedeemButton);
        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, RedeemActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultsActivity.this, MainMenu.class);
        startActivity(intent);
    }
}
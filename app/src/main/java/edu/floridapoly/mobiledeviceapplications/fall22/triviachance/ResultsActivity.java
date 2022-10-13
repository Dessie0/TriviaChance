package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    TextView numCorrectText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Intent intent = getIntent();
        int correct = intent.getIntExtra("CORRECT", 0);

        numCorrectText = findViewById(R.id.numCorrectText);
        numCorrectText.setText(String.format("You got %d correct", correct));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(correct * 25);

    }
}
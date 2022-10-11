package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    TextView questionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionText = findViewById(R.id.questionTextView);
        questionText.setText(R.string.question);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        Button answerButtons[] = {answer1, answer2, answer3, answer4};

        // right now has hardcoded strings
        // need a new way to get 4 answer choices
        String answers[] = getResources().getStringArray(R.array.answers);

        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(answers[i]);
        }
    }

    public void onClickAnswer(View view) {
        if (view.getId() == answer3.getId()) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            view.setBackgroundColor(Color.GREEN);
        }
        else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            view.setBackgroundColor(Color.RED);
        }

        Intent intent = new Intent(QuestionActivity.this, ResultsActivity.class);
        startActivity(intent);
    }
}
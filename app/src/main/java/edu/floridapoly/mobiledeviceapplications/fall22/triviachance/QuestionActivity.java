package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    Button[] answerButtons;
    TextView questionText;
    String[] questions;
    String[][] answers;

    int currentQuestionIndex = 0;
    int numberCorrect;
    int numberWrong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionText = findViewById(R.id.questionTextView);


        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        answerButtons = new Button[]{answer1, answer2, answer3, answer4};

        // right now has hardcoded strings
        // need a new way to get 4 answer choices
        answers = new String[][]{
                {"Squirrel", "Bat", "Phoenix", "Alligator"},
                {"Mars", "Earth", "Jupyter", "Venus"},
                {"bird alert", "tweet me", "twttr", "sweet tweet"},
                {"Fe", "Na", "K", "H"}};
        questions = getResources().getStringArray(R.array.questions);

        initQuestion(currentQuestionIndex);
    }

    public void initQuestion(int index) {
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setBackgroundColor(getResources().getColor(R.color.dark_blue));
            answerButtons[i].setTextColor(getResources().getColor(R.color.pewter));
            answerButtons[i].setText(answers[index][i]);
        }
        questionText.setText(questions[index]);

        for (int i = 0; i < 4; i++) {

        }
    }

    public void onClickAnswer(View view) {
        if (view.getId() == answer3.getId()) {
            //Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            view.setBackgroundColor(getResources().getColor(R.color.yellow));
            ((Button) view).setTextColor(getResources().getColor(R.color.dark_blue));
            numberCorrect++;
        }
        else {
            //Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            view.setBackgroundColor(getResources().getColor(R.color.red));
            numberWrong++;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentQuestionIndex < 3) {
                    initQuestion(++currentQuestionIndex);
                }
                else {
                    Intent intent = new Intent(QuestionActivity.this, ResultsActivity.class);
                    intent.putExtra("CORRECT", numberCorrect);
                    intent.putExtra("INCORRECT", numberWrong);
                    startActivity(intent);
                }
            }
        }, 500);



    }
}
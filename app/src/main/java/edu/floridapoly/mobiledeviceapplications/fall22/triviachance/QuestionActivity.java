package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    TypedValue typedValue;
    int colorPrimary, colorSecondary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_question);

        questionText = findViewById(R.id.questionTextView);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        questions = new String[10];
        answers = new String[10][4];
        answerButtons = new Button[]{answer1, answer2, answer3, answer4};

        typedValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        colorPrimary = typedValue.resourceId;
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        colorSecondary = typedValue.resourceId;

        //random trivia question API
        // can always change if we don't like the questions
        // gets 10 "easy" level questions as JSON array

    }

    public void initQuestion(int index) {
        //randomly shuffles indexes so buttons have random chance of being correct answer
        Integer[] randomIndexes = {0, 1, 2, 3};
        List<Integer> list = Arrays.asList(randomIndexes);
        Collections.shuffle(list);
        list.toArray(randomIndexes);

        for (int i = 0; i < 4; i++) {
            answerButtons[i].setBackgroundColor(getResources().getColor(colorPrimary));
            //answerButtons[i].setBackgroundColor(getResources().getColor(R.color.dark_blue));
            answerButtons[i].setTextColor(getResources().getColor(R.color.pewter));
            answerButtons[i].setText(answers[index][randomIndexes[i]]);
        }
        questionText.setText(questions[index]);
    }

    public void onClickAnswer(View view) {
        // 0 index in answers[index] is always set as the correct answer
        if (((Button) view).getText() == answers[currentQuestionIndex][0]) {
            view.setBackgroundColor(getResources().getColor(colorSecondary));
            ((Button) view).setTextColor(getResources().getColor(colorPrimary));
            numberCorrect++;
        }
        else {
            view.setBackgroundColor(getResources().getColor(R.color.red));
            numberWrong++;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentQuestionIndex < 9) {
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
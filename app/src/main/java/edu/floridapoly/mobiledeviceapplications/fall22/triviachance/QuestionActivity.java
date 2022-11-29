package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.TextQuestion;

public class QuestionActivity extends AppCompatActivity {
    ProgressBar questionProgress;

    private TriviaGame game;
    private Question<?> currentQuestion;

    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    Button[] answerButtons;
    TextView questionText;

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

        questionProgress = findViewById(R.id.questionProgressBar);
        questionProgress.setProgress(currentQuestionIndex);

        this.game = MainMenu.getAPI().getCurrentGame();

        questionText = findViewById(R.id.questionTextView);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        answerButtons = new Button[]{answer1, answer2, answer3, answer4};

        typedValue = new TypedValue();
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        colorPrimary = typedValue.resourceId;
        getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        colorSecondary = typedValue.resourceId;

        this.getNextQuestion().thenAccept(this::initQuestion);
    }

    public void initQuestion(Question<?> question) {
        //randomly shuffles indexes so buttons have random chance of being correct answer
        this.currentQuestion = question;
        questionText.setText(question.getQuestion());

        if(question instanceof TextQuestion) {
            TextQuestion textQuestion = (TextQuestion) question;

            int answerIndex = new Random().nextInt(4);
            int incorrectIndex = 0;

            for (int i = 0; i < 4; i++) {
                answerButtons[i].setBackgroundColor(getResources().getColor(colorPrimary));
                //answerButtons[i].setBackgroundColor(getResources().getColor(R.color.dark_blue));
                answerButtons[i].setTextColor(getResources().getColor(R.color.pewter));

                String text = i == answerIndex
                        ? textQuestion.getCorrectAnswer()
                        : textQuestion.getIncorrectAnswers().get(incorrectIndex++);
                answerButtons[i].setText(text);
            }
        }
    }

    public void onClickAnswer(View view) {
        Button clickedButton = (Button) view;
        if(this.getCurrentQuestion() instanceof TextQuestion) {
            TextQuestion textQuestion = (TextQuestion) this.getCurrentQuestion();
            if(textQuestion.attempt(clickedButton.getText().toString())) {
                this.markCorrect(clickedButton);
            } else {
                this.markIncorrect(clickedButton);
            }
        }


        /*
        TODO This could be improved, as right now we're just trusting that the server will
            return the question within the 500ms allotted, which is not guaranteed.
         */
        CompletableFuture<Question<?>> nextQuestion = this.getNextQuestion();
        new Handler().postDelayed(() -> {
            if (currentQuestionIndex++ < 9) {
                questionProgress.setSecondaryProgress(currentQuestionIndex * 10);
                ObjectAnimator.ofInt(questionProgress, "progress", currentQuestionIndex * 10).setDuration(700).start();
                initQuestion(nextQuestion.join());
            } else {
                Intent intent = new Intent(QuestionActivity.this, ResultsActivity.class);
                intent.putExtra("CORRECT", numberCorrect);
                intent.putExtra("INCORRECT", numberWrong);
                startActivity(intent);
            }
        }, 500);
    }

    private void markCorrect(Button button) {
        button.setBackgroundColor(getResources().getColor(colorSecondary));
        button.setTextColor(getResources().getColor(colorPrimary));
        numberCorrect++;
    }

    private void markIncorrect(Button button) {
        button.setBackgroundColor(getResources().getColor(R.color.red));
        numberWrong++;
    }

    private CompletableFuture<Question<?>> getNextQuestion() {
        return MainMenu.getInstancePackager().getAPI().retrieveQuestion(this.getGame(), this.currentQuestionIndex);
    }

    public TriviaGame getGame() {
        return game;
    }
    public Question<?> getCurrentQuestion() {
        return currentQuestion;
    }
}
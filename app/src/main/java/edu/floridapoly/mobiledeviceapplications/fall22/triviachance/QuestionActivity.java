package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.NextQuestionEvent;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.util.EventHandler;
import edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events.util.TriviaChanceListener;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.TextQuestion;

public class QuestionActivity extends AppCompatActivity implements TriviaChanceListener {
    ProgressBar questionProgress;

    private TriviaGame game;
    private Question<?> currentQuestion;
    private int currentQuestionId;

    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    Button[] answerButtons;
    TextView questionText;

    int numberCorrect;
    int numberWrong;

    TypedValue typedValue;
    int colorPrimary, colorSecondary;

    //Determines whether or not the 500ms of delay has passed for the next question to be initialized.
    private CompletableFuture<Void> canInitQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateTheme(this);
        setContentView(R.layout.activity_question);

        questionProgress = findViewById(R.id.questionProgressBar);
        questionProgress.setProgress(0);

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

        canInitQuestion = CompletableFuture.completedFuture(null);

        MainMenu.getAPI().registerListener(this);
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
                MainMenu.getAPI().getSocketInterface().submitQuestion(MainMenu.getLocalProfile(), this.game, this.currentQuestionId, true);
            } else {
                this.markIncorrect(clickedButton);
                MainMenu.getAPI().getSocketInterface().submitQuestion(MainMenu.getLocalProfile(), this.game, this.currentQuestionId, false);
            }
        }

        this.canInitQuestion = new CompletableFuture<>();

        new Handler().postDelayed(() -> {
            this.canInitQuestion.complete(null);
        }, 500);
    }

    private void markCorrect(Button button) {
        button.setBackgroundColor(getResources().getColor(colorSecondary));
        button.setTextColor(getResources().getColor(colorPrimary));
        if (MainMenu.getInstancePackager().getPreferences().getBoolean("sound", false)) {
            MediaPlayer correctChime = MediaPlayer.create(QuestionActivity.this, R.raw.correct);
            correctChime.start();
        }

        numberCorrect++;
    }

    private void markIncorrect(Button button) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && MainMenu.getInstancePackager().getPreferences().getBoolean("vibration", false)) {
            final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
        }

        if (MainMenu.getInstancePackager().getPreferences().getBoolean("sound", false)) {
            MediaPlayer incorrectChime = MediaPlayer.create(QuestionActivity.this, R.raw.incorrect);
            incorrectChime.start();
        }

        button.setBackgroundColor(getResources().getColor(R.color.red));
        numberWrong++;
    }

    private void openResults() {
        Intent intent;
        if (!this.game.isOnline()) {
            intent = new Intent(QuestionActivity.this, SoloResultsActivity.class);
            intent.putExtra("CORRECT", numberCorrect);
            intent.putExtra("INCORRECT", numberWrong);

            //Leave the game, since it's finished.
            startActivity(intent);
        }
        else {
            intent = new Intent(QuestionActivity.this, OnlineResultsActivity.class);
        }

        MainMenu.getAPI().unregisterListener(this);
        MainMenu.getAPI().leaveGame(MainMenu.getLocalProfile(), MainMenu.getAPI().getCurrentGame());
        startActivity(intent);
    }

    @EventHandler
    public void onNextQuestion(NextQuestionEvent event) {
        this.canInitQuestion.thenRun(() -> {
            if(event.getQuestionId() == -1) {
                this.openResults();
            } else {
                this.runOnUiThread(() -> {
                    questionProgress.setSecondaryProgress(event.getQuestionId() * 10);
                    ObjectAnimator.ofInt(questionProgress, "progress", event.getQuestionId() * 10).setDuration(700).start();
                });

                initQuestion(event.getQuestion());
                this.currentQuestionId = event.getQuestionId();
            }
        });
    }

    public TriviaGame getGame() {
        return game;
    }
    public Question<?> getCurrentQuestion() {
        return currentQuestion;
    }
}
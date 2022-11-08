package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions;

import java.util.List;

public class TextQuestion extends Question<String> {
    public TextQuestion(String question, String correctAnswer, List<String> incorrectAnswers) {
        super(question, correctAnswer, incorrectAnswers);
    }

    @Override
    public boolean attempt(String answer) {
        return this.getCorrectAnswer().equalsIgnoreCase(answer);
    }
}

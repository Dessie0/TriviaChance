package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions;

import java.util.List;

public abstract class Question<T> {

    private final String question;
    private final T correctAnswer;
    private final List<T> incorrectAnswers;

    public Question(String question, T correctAnswer, List<T> incorrectAnswers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public abstract boolean attempt(T answer);

    public String getQuestion() {
        return question;
    }
    public T getCorrectAnswer() {
        return correctAnswer;
    }
    public List<T> getIncorrectAnswers() {
        return incorrectAnswers;
    }

}

package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.options.QuestionOption;

public class Question<T extends QuestionOption> {
    private final int id;
    private final String question;

    private final Class<T> answerType;
    private final List<T> options;
    private final int correctOption;

    public Question(int id, String question, Class<T> answerType, List<T> options, int correctOption) {
        this.id = id;
        this.question = question;
        this.answerType = answerType;
        this.options = options;
        this.correctOption = correctOption;
    }

    public int getId() {
        return id;
    }
    public String getQuestion() {
        return question;
    }
    public Class<T> getAnswerType() {
        return answerType;
    }
    public List<T> getOptions() {
        return options;
    }
    public int getCorrectOption() {
        return correctOption;
    }
}

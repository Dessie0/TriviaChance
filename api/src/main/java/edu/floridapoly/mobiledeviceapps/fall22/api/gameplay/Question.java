package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

public class Question<T> {
    private final int id;
    private final Class<T> answerType;
    private final String question;

    public Question(int id, String question, Class<T> answerType) {
        this.id = id;
        this.question = question;
        this.answerType = answerType;
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
}

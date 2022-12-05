package edu.floridapoly.mobiledeviceapps.fall22.api.gameplay;

public class GameStats {

    private int correct;
    private int incorrect;

    public GameStats() {
        this.correct = 0;
        this.incorrect = 0;

    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }
    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public int getCorrect() {
        return correct;
    }
    public int getIncorrect() {
        return incorrect;
    }
}

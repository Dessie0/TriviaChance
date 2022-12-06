package edu.floridapoly.mobiledeviceapplications.fall22.triviachance.api.events;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;

public class NextQuestionEvent extends GameEvent {

    private final Question<?> question;
    private final int questionId;

    public NextQuestionEvent(TriviaGame game, Question<?> question, int questionId) {
        super(game);
        this.question = question;
        this.questionId = questionId;
    }

    public Question<?> getQuestion() {
        return question;
    }

    public int getQuestionId() {
        return questionId;
    }
}

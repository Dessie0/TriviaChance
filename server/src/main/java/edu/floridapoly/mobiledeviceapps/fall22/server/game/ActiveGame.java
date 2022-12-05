package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.Player;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.TriviaGame;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.MessageType;
import edu.floridapoly.mobiledeviceapps.fall22.api.socket.SocketMessageGenerator;

public class ActiveGame {

    private static final int MAX_QUESTIONS = 10;

    private final TriviaGame game;
    private final Map<Player, WebSocket> players;
    private final QuestionRandomizer questionRandomizer;

    private int currentQuestion = 0;
    private final Map<Integer, List<Player>> submittedQuestions;

    private boolean started;

    public ActiveGame(TriviaGame game) {
        this.game = game;
        this.players = new HashMap<>();
        this.submittedQuestions = new HashMap<>();
        this.questionRandomizer = new QuestionRandomizer();
    }

    public void addPlayer(Player player, WebSocket socket) {
        this.getPlayers().put(player, socket);
    }

    public void removePlayer(String uuid) {
        this.getPlayers().entrySet().removeIf(entry -> entry.getKey().getProfile().getUUID().toString().equalsIgnoreCase(uuid));
    }

    public Player findPlayer(String uuid) {
        return this.getPlayers().keySet().stream().filter(player -> player.getProfile().getUUID().toString().equalsIgnoreCase(uuid))
                .findFirst().orElse(null);
    }

    public void submit(Player player, int questionId) {
        this.getSubmittedQuestions().putIfAbsent(questionId, new ArrayList<>());
        this.getSubmittedQuestions().get(questionId).add(player);

        //Check if all players have submitted the question
        for (Player p : this.getPlayers().keySet()) {
            if (!this.getSubmittedQuestions().get(questionId).contains(p)) {
                return;
            }
        }

        //If all players have submitted, then send the next question.
        this.nextQuestion();
    }

    public void nextQuestion() {
        if(this.currentQuestion + 1 > MAX_QUESTIONS) {
            for (Map.Entry<Player, WebSocket> entry : this.getPlayers().entrySet()) {
                entry.getValue().send(new SocketMessageGenerator(MessageType.NEXT_QUESTION)
                        .setParam("gameUUID", this.getGame().getUUID().toString())
                        .setParam("question", null)
                        .setParam("questionId", "-1")
                        .generate());
            }
        } else {
            this.getQuestionRandomizer().getQuestion(this.currentQuestion++).thenAccept(question -> {
                String json = new Gson().toJson(question);

                for (Map.Entry<Player, WebSocket> entry : this.getPlayers().entrySet()) {
                    entry.getValue().send(new SocketMessageGenerator(MessageType.NEXT_QUESTION)
                            .setParam("gameUUID", this.getGame().getUUID().toString())
                            .setParam("question", json)
                            .setParam("questionId", String.valueOf(this.currentQuestion - 1))
                            .generate());
                }
            });
        }

        //TODO Restart question timer.

    }


    private Map<Integer, List<Player>> getSubmittedQuestions() {
        return submittedQuestions;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }
    public WebSocket getWebSocket(Player player) {
        return this.getPlayers().get(player);
    }
    public QuestionRandomizer getQuestionRandomizer() {
        return questionRandomizer;
    }
    public TriviaGame getGame() {
        return game;
    }
    public Map<Player, WebSocket> getPlayers() {
        return players;
    }


}

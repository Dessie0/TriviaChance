package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;
import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.TextQuestion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionRandomizer {

    //TODO make sure duplicate questions are not generated.

    private final CompletableFuture<Void> isBankGenerated = new CompletableFuture<>();
    private List<Question<?>> questionBank;

    public QuestionRandomizer() {
        this.generateQuestionBank().thenAccept(bank -> {
            this.questionBank = bank;
            this.isBankGenerated.complete(null);
        });
    }

    public CompletableFuture<Question<?>> getQuestion(int index) {
        CompletableFuture<Question<?>> future = new CompletableFuture<>();

        if(this.getQuestionBank() != null) {
            return CompletableFuture.completedFuture(this.getQuestionBank().get(index));
        } else {
            this.isBankGenerated.thenRun(() -> {
                future.complete(this.getQuestionBank().get(index));
            });
            return future;
        }
    }

    public List<Question<?>> getQuestionBank() {
        return questionBank;
    }

    private CompletableFuture<List<Question<?>>> generateQuestionBank() {
//        //random trivia question API
//        // can always change if we don't like the questions
//        // gets 10 "easy" level questions as JSON array

        CompletableFuture<List<Question<?>>> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://the-trivia-api.com/api/questions?limit=10&difficulty=easy")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                JsonArray array = JsonParser.parseString(body).getAsJsonArray();

                //Since we know all questions are going to be TextOptions, we'll manually create that for now.
                List<Question<?>> questions = new ArrayList<>();

                for(JsonObject object : array.asList().stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())) {
                    questions.add(new TextQuestion(object.get("question").getAsString(),
                            object.get("correctAnswer").getAsString(),
                            object.get("incorrectAnswers").getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.toList())));
                }

                future.complete(questions);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

}

package edu.floridapoly.mobiledeviceapps.fall22.server.game;

import edu.floridapoly.mobiledeviceapps.fall22.api.gameplay.questions.Question;

public class QuestionRandomizer {
    
    //TODO make sure duplicate questions are not generated.
    
    public Question<?> generateQuestion() {
//        //random trivia question API
//        // can always change if we don't like the questions
//        // gets 10 "easy" level questions as JSON array
//        String url = "https://the-trivia-api.com/api/questions?limit=10&difficulty=easy";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        JsonArrayRequest questionRequest = new JsonArrayRequest(Request.Method.GET,
//                url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                // setts the questions and answers array to the values gathered
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject currentQuestion = response.getJSONObject(i);
//                        questions[i] = currentQuestion.getString("question");
//                        answers[i][0] = currentQuestion.getString("correctAnswer");
//                        for (int j = 1; j < 4; j++) {
//                            answers[i][j] = currentQuestion.getJSONArray("incorrectAnswers").getString(j-1);
//                        }
//                        initQuestion(currentQuestionIndex);
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //code for an error
//                Toast.makeText(getBaseContext(), "Failed to gather question data", Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(questionRequest);

        return null;
    }
    
}

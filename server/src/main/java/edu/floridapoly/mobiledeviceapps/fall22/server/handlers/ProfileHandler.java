package edu.floridapoly.mobiledeviceapps.fall22.server.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ProfileHandler implements TriviaChanceHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange.getRequestURI().getQuery());

        JsonObject object = new JsonObject();
        object.addProperty("id", params.get("id"));

        exchange.sendResponseHeaders(200, object.toString().getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(object.toString().getBytes());
        os.close();
    }
}

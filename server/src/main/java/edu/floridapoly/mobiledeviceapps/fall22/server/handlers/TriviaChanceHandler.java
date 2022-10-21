package edu.floridapoly.mobiledeviceapps.fall22.server.handlers;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;

public abstract class TriviaChanceHandler implements HttpHandler {

    private final TriviaChanceServer server;

    public TriviaChanceHandler(TriviaChanceServer server) {
        this.server = server;
    }

    public Map<String, String> getParams(String request) {
        if(request == null) return new HashMap<>();

        String[] requests = request.split("&");
        return Arrays.stream(requests).collect(Collectors.toMap(
                (value) -> value.split("=")[0], (value) -> value.split("=")[1]));
    }

    public void sendResponse(HttpExchange exchange, JsonObject object) throws IOException {
        exchange.sendResponseHeaders(200, object.toString().getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(object.toString().getBytes());
        os.close();
    }

    public void sendNotFoundError(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(404, message.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }

    public TriviaChanceServer getServer() {
        return server;
    }
}

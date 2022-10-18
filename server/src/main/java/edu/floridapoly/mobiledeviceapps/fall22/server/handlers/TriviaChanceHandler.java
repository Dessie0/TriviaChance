package edu.floridapoly.mobiledeviceapps.fall22.server.handlers;

import com.sun.net.httpserver.HttpHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface TriviaChanceHandler extends HttpHandler {
    default Map<String, String> getParams(String request) {
        if(request == null) return new HashMap<>();

        String[] requests = request.split("&");
        return Arrays.stream(requests).collect(Collectors.toMap(
                (value) -> value.split("=")[0], (value) -> value.split("=")[1]));
    }
}

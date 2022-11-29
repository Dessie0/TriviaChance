package edu.floridapoly.mobiledeviceapps.fall22.server.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;

public class PingHandler extends TriviaChanceHandler {
    public PingHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.sendResponse(httpExchange, "true");
    }
}

package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.image;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class ImageDownloadHandler extends TriviaChanceHandler {
    public ImageDownloadHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String fileName = exchange.getRequestURI().toString().split("/")[3];
        File file = new File(ICON_FOLDER + fileName);

        exchange.sendResponseHeaders(200, file.length());
        exchange.getResponseHeaders().set("Content-Type", "image/jpeg");

        OutputStream stream = exchange.getResponseBody();
        Files.copy(file.toPath(), stream);
        stream.close();
    }
}

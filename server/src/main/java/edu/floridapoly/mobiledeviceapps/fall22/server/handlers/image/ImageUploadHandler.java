package edu.floridapoly.mobiledeviceapps.fall22.server.handlers.image;

import com.sun.net.httpserver.HttpExchange;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import edu.floridapoly.mobiledeviceapps.fall22.server.TriviaChanceServer;
import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.TriviaChanceHandler;

public class ImageUploadHandler extends TriviaChanceHandler {
    public ImageUploadHandler(TriviaChanceServer server) {
        super(server);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = this.getParams(exchange);

        String base64 = params.get("base64");
        byte[] bytes = Base64.getDecoder().decode(base64);

        UUID uuid = UUID.randomUUID();

        //Write the image
        FileOutputStream fos = new FileOutputStream(ICON_FOLDER + uuid + ".jpg");
        fos.write(bytes);
        fos.close();

        this.sendResponse(exchange, "http://51.79.52.211:8082/image/download/" + uuid + ".jpg");
    }
}

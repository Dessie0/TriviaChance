package edu.floridapoly.mobiledeviceapps.fall22.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.floridapoly.mobiledeviceapps.fall22.server.handlers.ProfileHandler;

public class TriviaChanceServer {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/profile", new ProfileHandler());

            System.out.println("Server started.");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
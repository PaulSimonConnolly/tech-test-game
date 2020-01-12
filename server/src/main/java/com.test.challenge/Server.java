package com.test.challenge;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws Exception {

        try (ServerSocket listener = new ServerSocket(8080)) {
            ExecutorService threadPool = Executors.newFixedThreadPool(200);
            System.out.println("Server is Running.......");
            while (true) {
                Game game = new Game();
                threadPool.execute(game.new Player(listener.accept(), "X"));
                threadPool.execute(game.new Player(listener.accept(), "O"));
            }
        }
    }
}
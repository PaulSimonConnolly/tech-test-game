package com.test.challenge;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main start class of the client applications
 */
public class Application {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Requires IP: i.e. 127.0.0.1 or localhost");
            return;
        }

        final Socket socket = new Socket(args[0], 8080);
        final Scanner scanner = new Scanner(socket.getInputStream());
        final PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        final Scanner keyboardInput = new Scanner(System.in);
        final Client client = new Client(socket, scanner, printWriter, keyboardInput);
        client.promptUserToInputName();
        client.play();
    }
}

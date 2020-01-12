package com.test.challenge;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Client controls user input, displaying, and sends and receives messages from the server
 */
public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Scanner keyboardInput;

    public Client(Socket socket, Scanner scanner, PrintWriter printWriter, Scanner keyboardInput) {
        this.socket = socket;
        this.in = scanner;
        this.out = printWriter;
        this.keyboardInput = keyboardInput;
    }

    /**
     * Prompt user to input name
     */
    public void promptUserToInputName() {
        System.out.print("Please enter your name: ");
        final String name = keyboardInput.nextLine();
        out.println("USERNAME:" + name);
    }

    /**
     * Play the game
     * @throws Exception
     */
    public void play() throws Exception {
        try {
            String response;
            while (in.hasNextLine()) {
                response = in.nextLine();
                if (response.startsWith("PLEASE_WAIT")) {
                    printMessage("Please wait for your turn");
                }else if (response.startsWith("MESSAGE-TURN")){
                    printMessageAndRequireInput(response.split(":")[1]);
                }
                else if (response.startsWith("MESSAGE")) {
                    printMessage(response.split(":")[1]);
                } else if (response.startsWith("GAME_OVER")) {
                    printMessage(response.split(":")[1]);
                    break;
                } else if (response.startsWith("DRAW")) {
                    printMessage("The game is a draw");
                    break;
                } else if (response.startsWith("OPPONENT_DISCONNECT")) {
                    printMessage("Opponent disconnected, YOU WIN!");
                    break;
                } else if (response.startsWith("BOARD")) {
                    boardToConsole(response);
                }else if(response.startsWith("COLUMN_FULL")){
                    printMessageAndRequireInput("Column is full, please select another column");
                }
            }
            out.println("QUIT_GAME");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    /**
     * Prints a new line on the console
     */
    private void newlineOnScreen() {
        System.out.printf("%n");
    }

    /**
     * Prints a message to the console
     * @param message String
     */
    private void printMessage(final String message) {
        System.out.println(message);
    }

    /**
     * Prints a message and requests user input
     * @param message String
     */
    private void printMessageAndRequireInput(final String message) {
        System.out.println(message);
        String selection = getUserColumnSelection();
        out.println("MOVE:" + selection);
    }

    /**
     * Requests the user to input a selection between 1 and 9
     * @return String
     */
    private String getUserColumnSelection() {
        boolean flag;
        String input;
        do {
            System.out.print("Please select between 1 - 9: ");
            input = keyboardInput.next();
            flag = input.matches("[1-9]");
            if (!flag) System.out.println("You must enter a number!");
        } while (!flag);
        return input;
    }

    /**
     * Prints the board to the console
     * @param message String
     */
    private void boardToConsole(String message) {
        newlineOnScreen();
        if (message.startsWith("BOARD")) {
            newlineOnScreen();
            final String[][] board = convert(message.split(":")[1]);
            for (final String[] row : board) {
                System.out.println(Arrays.toString(row));
            }
            newlineOnScreen();
        }
    }

    /**
     * Converts the board from string array to 2D array for easier printing
     * @param boardData String
     * @return String[][]
     */
    public String[][] convert(String boardData){
        return Arrays.stream(boardData.substring(2, boardData.length() - 2).split("\\], \\["))
                .map(e -> Arrays.stream(e.split(",")).toArray(String[]::new)).toArray(String[][]::new);
    }
}
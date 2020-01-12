package com.test.challenge;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private static final int ROWS = 6;
    private static final int COLUMNS = 9;
    private String[][] board;
    private Player currentPlayer;

    public Game() {
        board = new String[ROWS][COLUMNS];
        for (String[] row : board) {
            Arrays.fill(row, " ");
        }
    }

    public boolean boardFilledUp() {
        return Arrays.stream(board).allMatch(str -> str.equals(" "));
    }

    public synchronized void move(int columnSelection) throws ColumnFullException {
        if (!insertIntoBoard(columnSelection)) {
            throw new ColumnFullException("Column Full");
        }
        currentPlayer = currentPlayer.opponent;
    }

    class Player implements Runnable {
        String mark;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;
        String name;

        public Player(Socket socket, String mark) {
            this.socket = socket;
            this.mark = mark;
        }

        @Override
        public void run() {
            try {
                initPlayer();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (opponent != null && opponent.output != null) {
                    opponent.output.println("OPPONENT_DISCONNECT");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void initPlayer() throws IOException {
            System.out.println("Initializing player");
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("MARKER:" + mark);
            if (mark.equals("X")) {
                currentPlayer = this;
                output.println("MESSAGE:Waiting for opponent to connect");
            } else {
                opponent = currentPlayer;
                opponent.opponent = this;
                sendBoardUpdate();
                output.println("MESSAGE:It's " + opponent.name + "'s move, please wait");
                opponent.output.println("MESSAGE-TURN:It's your move " + opponent.name +", you are "+opponent.mark);
            }
        }

        private void processCommands() {
            while (input.hasNextLine()) {
                String message = input.nextLine();
                if (isCommand(message, "QUIT_GAME")) {
                    System.out.println("Quit game received");
                    return;
                } else if (isCommand(message, "MOVE")) {
                    System.out.println("Move received");
                    processMoveCommand(Integer.parseInt(message.split(":")[1]));
                } else if (isCommand(message, "USERNAME")) {
                    System.out.println("Setting user name");
                    name = message.split(":")[1];
                }
            }
        }

        private void processMoveCommand(int location) {
            try {
                move(location);
                sendBoardUpdate();
                if (BoardWinLogic.isWinner(board, ROWS, COLUMNS)) {
                    System.out.println(name + " is the WINNER, hard luck " + opponent.name);
                    output.println("GAME_OVER:" + name + " is the WINNER, hard luck " + opponent.name);
                    opponent.output.println("GAME_OVER:" + name + " is the WINNER, hard luck " + opponent.name);
                } else if (boardFilledUp()) {
                    System.out.println("Game is draw");
                    output.println("DRAW");
                    opponent.output.println("DRAW");
                } else {
                    output.println("PLEASE_WAIT");
                    opponent.output.println("MESSAGE-TURN:It's your move " + opponent.name +", you are "+opponent.mark);
                }
            } catch (IllegalStateException e) {
                output.println("MESSAGE:" + e.getMessage());
            } catch (ColumnFullException e) {
                output.println("COLUMN_FULL");
            }
        }

        public void sendBoardUpdate() {
            System.out.println("Sending board update to clients");
            String boardAsString = Arrays.deepToString(board);
            output.println("BOARD:" + boardAsString);
            opponent.output.println("BOARD:" + boardAsString);
        }
    }

    public boolean isCommand(final String message, final String command) {
        return message.startsWith(command);
    }

    public boolean insertIntoBoard(final int columnSelection) {
        final int selection = columnSelection - 1;
        if (!board[0][selection].equals(" "))
            return false;
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][selection].equals(" ")) {
                board[row][selection] = currentPlayer.mark;
                break;
            }
        }
        return true;
    }
}
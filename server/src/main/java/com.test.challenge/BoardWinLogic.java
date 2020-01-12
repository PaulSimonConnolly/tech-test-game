package com.test.challenge;

public final class BoardWinLogic {

    public static boolean isWinner(final String[][] board, final int numRows, final int numCols) {
        boolean isWin = false;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                String boardValue = board[r][c];
                if (!boardValue.equals(" ")) {
                    if (checkRowForWinner(board, numCols, r, c) || checkColumnForWinner(board, numRows, r, c)) {
                        isWin = true;
                    }
                }
            }
        }
        return isWin;
    }

    private static boolean checkColumnForWinner(final String[][] board, final int numRows, int r, int c) {
        String value = board[r][c];
        int matches = 0;
        for (int i = r; i < numRows; i++) {
            if (value.equals(board[i][c])) {
                matches++;
            } else {
                break;
            }
        }
        return isNumberMatchesWinner(matches);
    }

    private static boolean checkRowForWinner(final String[][] board, final int numCols, final int r, int c) {
        String value = board[r][c];
        int matches = 0;
        for (int i = c; i < numCols; i++) {
            if (value.equals(board[r][i])) {
                matches++;
            } else {
                break;
            }
        }
        return isNumberMatchesWinner(matches);
    }

    private static boolean isNumberMatchesWinner(final int matches) {
        return matches >= 5;
    }
}

package com.pennypop.project.GUI.connect4board;

import com.pennypop.project.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InternalBoard {
    private final char[][] board;
    public static final char AI = '2';
    public static final char PLAYER = '1';
    public static final char EMPTY = '0';
    private int emptyCells = 0;


    public InternalBoard(char[][] board, int emptyCells) {
        this.board = Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);
        this.emptyCells = emptyCells;
    }

    public InternalBoard(Cell[][] guiBoard) {
        int width = guiBoard.length;
        int height = guiBoard[0].length;
        this.board = new char[height][width];
        this.emptyCells = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[height - j - 1][i] = (guiBoard[i][j].getOccupied() == Config.Player.PLAYER ? PLAYER :
                        (guiBoard[i][j].getOccupied() == Config.Player.AI ? AI :
                                EMPTY)
                );
                if (this.board[height - j - 1][i] == EMPTY) {
                    this.emptyCells++;
                }
            }
        }
    }

    private int getAvailableRow(int col) {
        for (int i = board.length - 1; i >= 0; i--) {
            if (isEmpty(i, col)) {
                return i;
            }
        }
        return -1;
    }

    private boolean addPiece(int col, char piece) {
        int row = getAvailableRow(col);
        if (row == -1) {
            return false;
        }
        board[row][col] = piece;
        emptyCells--;
        return true;
    }

    public List<InternalBoard> getNextMoves(char turn) {
        List<InternalBoard> nextMoves = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            if (canPlay(i)) {
                InternalBoard nextBoard = new InternalBoard(board, this.emptyCells);
                nextBoard.addPiece(i, turn);
                nextMoves.add(nextBoard);
            } else {
                nextMoves.add(null);
            }
        }
        return nextMoves;
    }

    public boolean isCompleteFours(int row, int col, int rowDirection, int colDirection) {
        if (isEmpty(row, col)) return false;
        char currentPlayer = board[row][col];
        for (int i = 0; i < 4; i++) {
            if (row < 0 || row >= height() || col < 0 || col >= width() || board[row][col] != currentPlayer)
                return false;
            row += rowDirection;
            col += colDirection;
        }
        return true;
    }

    public int getCompleteFours(int row, int col) {
        return (isCompleteFours(row, col, 0, 1) ? 1 : 0) +
                (isCompleteFours(row, col, 1, 0) ? 1 : 0) +
                (isCompleteFours(row, col, 1, 1) ? 1 : 0) +
                (isCompleteFours(row, col, 1, -1) ? 1 : 0);
    }

    private int getScore(char player) {
        int score = 0;
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                if (board[i][j] == player) {
                    score += getCompleteFours(i, j);
                }
            }
        }
        return score;
    }


    public String hash() {
        String concatenate = "";
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                concatenate = concatenate.concat("" + board[i][j]);
            }
        }
        return concatenate;
    }

    public void printState() {
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean canPlay(int col) {
        return getAvailableRow(col) != -1;
    }

    public boolean isPlayer(int row, int col) {
        return board[row][col] == PLAYER;
    }

    public boolean isAI(int row, int col) {
        return board[row][col] == AI;
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == EMPTY;
    }

    public int getPlayerScore() {
        return getScore(PLAYER);
    }

    public int getAIScore() {
        return getScore(AI);
    }

    public int height() {
        return board.length;
    }

    public int width() {
        return board[0].length;
    }

    public int getEmptyCells() {
        return emptyCells;
    }
}

package com.pennypop.project.GUI.connect4board;

import com.pennypop.project.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InternalBoard {
    private char[][] board;
    public static final char AI = '2';
    public static final char PLAYER = '1';
    public static final char EMPTY = '0';

    public InternalBoard(char[][] board) {
        this.board = Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);

    }

    public InternalBoard(Cell[][] guiBoard) {
        int width = guiBoard.length;
        int height = guiBoard[0].length;
        this.board = new char[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[height - j - 1][i] = (guiBoard[i][j].getOccupied() == Config.Player.PLAYER ? PLAYER :
                        (guiBoard[i][j].getOccupied() == Config.Player.AI ? AI :
                                EMPTY)
                );
            }
        }
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

    public boolean addAI(int col) {
        return addPiece(col, AI);
    }

    public boolean addPlayer(int col) {
        return addPiece(col, PLAYER);
    }

    private int getAvailableRow(int col) {
        for (int i = board.length - 1; i >= 0; i--) {
            if (isEmpty(i, col)) {
                return i;
            }
        }
        return -1;
    }

    private boolean canPlay(int col) {
        return getAvailableRow(col) != -1;
    }

    private boolean addPiece(int col, char piece) {
        int row = getAvailableRow(col);
        if (row == -1) {
            return false;
        }
        board[row][col] = piece;
        return true;
    }

    public List<InternalBoard> getNextMoves(char turn) {
        List<InternalBoard> nextMoves = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            if (canPlay(i)) {
                InternalBoard nextBoard = new InternalBoard(board);
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
            if (row < 0 || row >= height() || col < 0 || col >= width() || board[row][col] != currentPlayer) return false;
            row += rowDirection;
            col += colDirection;
        }
        return true;
    }


    public int height() {
        return board.length;
    }

    public int width() {
        return board[0].length;
    }
}

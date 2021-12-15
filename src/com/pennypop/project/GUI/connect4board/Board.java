package com.pennypop.project.GUI.connect4board;

import com.badlogic.gdx.Gdx;
import com.pennypop.project.Config;

public class Board {
    Cell[][] guiBoard;
    int width, height;

    public Board(int width, int height) {
        this.height = height;
        this.width = width;
        createGrid();
    }

    public void createGrid() {
        int x = (Gdx.graphics.getWidth() / 2) - ((width * Cell.SIZE + (width - 1) * Cell.MARGIN) / 2);
        int y = (Gdx.graphics.getHeight() / 2) - ((height * Cell.SIZE) / 2);
        guiBoard = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                guiBoard[i][j] = new Cell(i, j, x, y);
                y += Cell.SIZE + Cell.MARGIN;
            }
            y = (Gdx.graphics.getHeight() / 2) - ((height * Cell.SIZE) / 2);
            x += Cell.SIZE + Cell.MARGIN;
        }
    }


    public Config.Player getOccupier(int x, int y) {
        return guiBoard[x][y].getOccupied();
    }

    public void setOccupier(int x, int y, Config.Player occupier) {
        guiBoard[x][y].setCell(occupier);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public InternalBoard toInternalBoardGrid() {
        return new InternalBoard(guiBoard);
    }
}

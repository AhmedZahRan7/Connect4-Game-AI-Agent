package com.pennypop.project.controller;

import com.pennypop.project.Config;
import com.pennypop.project.GUI.connect4board.Board;
import com.pennypop.project.GUI.GameScreen;

public class GameLogic {
	private final Board board;
	private Config.Player turn = Config.Player.AI;
	private int turnNo = 0; // track number of turns
	private final AI ai;
	private final GameScreen gui;

	public GameLogic(Board board, boolean withPruning, GameScreen gui) {
		this.board = board;
		this.gui = gui;
		ai = new AI(withPruning,this);
		if(turn == Config.Player.AI){
			ai.makeMove(board.toInternalBoardGrid());
		}
	}

	public void placePiece(int x) {
		// We search the selected column for an available position to drop the
		// piece starting from the bottom
		for (int i = 0; i < board.getHeight(); i++) {
			if (board.getOccupier(x,i) == Config.Player.EMPTY) {
				// If a valid spot is found, set the Cell to the current
				// player's color
				board.setOccupier(x, i, turn);
				turnNo++;
				if (isFinished()) {
					int player = board.toInternalBoardGrid().getPlayerScore();
					int AIScore = board.toInternalBoardGrid().getAIScore();
					gui.showResult(player,AIScore);
				}
				this.switchTurns();
				if (turn == Config.Player.AI) {
					ai.makeMove(board.toInternalBoardGrid());
				}
			}
		}
	}
	void switchTurns() {
		turn = (turn == Config.Player.AI? Config.Player.PLAYER : Config.Player.AI);
	}
	boolean isFinished() {
		return turnNo == board.getHeight() * board.getWidth();
	}

	public int getNumRows() {
		return board.getHeight();
	}
	public int getNumColumns() {
		return board.getWidth();
	}
}

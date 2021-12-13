package com.pennypop.project.controller;

import com.pennypop.project.Config;
import com.pennypop.project.GUI.connect4board.Board;
import com.pennypop.project.GUI.GameScreen;

public class GameLogic {
	private final Board board;
	private Config.Player turn = Config.Player.AI;
	private int turnNo = 0; // track number of turns
	private final AI ai; // may or may not be initialized depending on game type
	private final GameScreen gui;
//	private boolean gameOver = false;

	public GameLogic(Board board,GameScreen gui) {
		this.board = board;
		this.gui = gui;
		ai = new AI(this);
		if(turn == Config.Player.AI){
			ai.makeMove(board.toInternalBoardGrid());
		}
	}
	/**
	 * This method consults game state to determine a valid move.
	 * 
	 * Searches the column selected by the user for the first available row
	 * 
	 * @param x
	 *            coordinate of selected player Cell
//	 * @param y
	 *            coordinate of selected player Cell
	 * @return true if there's a valid move based on player input, false
	 *         otherwise
	 */
	public boolean placePiece(int x) {
//		if (gameOver) return false;
		// We search the selected column for an available position to drop the
		// piece starting from the bottom
		for (int i = 0; i < board.getHeight(); i++) {
			if (board.getOccupier(x,i) == Config.Player.EMPTY) {
				// If a valid spot is found, set the Cell to the current
				// player's color
				board.setOccupier(x,i,turn);
				turnNo++;
				if (turnNo == board.getHeight()*board.getWidth()) {
					int player = board.toInternalBoardGrid().getPlayerScore();
					int AIScore = board.toInternalBoardGrid().getAIScore();
					gui.showResult(player,AIScore);
//					victory(Config.Player.EMPTY);
				}
				turn = turn == Config.Player.AI? Config.Player.PLAYER : Config.Player.AI;
				if (turn == Config.Player.AI) {
					ai.makeMove(board.toInternalBoardGrid());
				}
				return true;
			}
		}
		return false;
	}

//	public void victory(int player1) {
////		gameOver = true;
//		gui.showWinner(player);
//
//	}
	public int isVictory(int n, int x, int y) {

		return 0;
//		int maxConsec = 1;
//		int thisColor = board.getOccupier(x,y);
//		int consec = 0;
//
//		/*-----------------Check horizontal-------------*/
//		for (int i = 0; i < board.getWidth(); i++) {
//			if (board.getOccupier(i,y)== thisColor) {
//				consec++;
//				if (consec > maxConsec) {
//					maxConsec = consec;
//				}
//				if (maxConsec == n) {
//					return maxConsec;
//				}
//			} else {
//				consec = 0;
//			}
//		}
//
//		/*-------------------Check vertical-------------*/
//		consec = 0;
//		for (int i = 0; i < board.getHeight(); i++) {
//			if (board.getOccupier(x,i) == thisColor) {
//				consec++;
//				if (consec > maxConsec) {
//					maxConsec = consec;
//				}
//				if (maxConsec == n) {
//					return maxConsec;
//				}
//			} else {
//				consec = 0;
//			}
//		}
//
//		/* ------Check Diagonal in this direction -> "/"--- */
//		consec = 0;
//		int curX = x;
//		int curY = y;
//		// first find starting point
//		while (curX > 0 && curY > 0) {
//			curX--;
//			curY--;
//		}
//		int startX = curX;
//		int startY = curY;
//		// Now begin check from bottom left to top right
//		while (startX < board.getWidth() && startY < board.getHeight()) {
//			if (board.getOccupier(startX,startY) == thisColor) {
//				consec++;
//				if (consec > maxConsec) {
//					maxConsec = consec;
//				}
//				if (maxConsec == n) {
//					return maxConsec;
//				}
//			} else {
//				consec = 0;
//			}
//			startX++;
//			startY++;
//		}
//
//		/*--------Check Diagonal in this direction -> "\" ----*/
//		// first find starting point
//		curX = x;
//		curY = y;
//		consec = 0;
//		while (curX < board.getWidth() - 1 && curY > 0) {
//			curX++;
//			curY--;
//		}
//		startX = curX;
//		startY = curY;
//		// Now begin check from bottom right to top left
//		while (startX >= 0 && startY < board.getHeight()) {
//			if (board.getOccupier(startX,startY) == thisColor) {
//				consec++;
//				if (consec > maxConsec) {
//					maxConsec = consec;
//				}
//				if (maxConsec == n) {
//					return maxConsec;
//				}
//			} else {
//				consec = 0;
//			}
//			startX--;
//			startY++;
//		}
//		return maxConsec;
	}
	public int getNumRows() {
		return board.getHeight();
	}
	public int getNumColumns() {
		return board.getWidth();
	}
}

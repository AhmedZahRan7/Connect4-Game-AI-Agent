package com.pennypop.project.controller;

import java.util.Random;

/**
 * A really really bad connect 4 AI Just makes random moves for now
 * 
 * I plan on adding the 'intelligence' part of AI later, but currently I'm out
 * of time This class is mostly intended to show that my Game model is
 * compatible with AI
 * 
 * @author Kevin
 *
 */
public class AI {
	private GameLogic g;

	public AI(GameLogic g) {
		this.g = g;
	}

	/**
	 * Makes a random move
	 */
	public void makeMove() {
		int rows = g.getNumRows();
		int columns = g.getNumColumns();

		Random rand = new Random();
		int randomNum = rand.nextInt(columns);

		boolean moved = false;
		// attempts to drop piece at the top of a random row until it
		// successfully places one
		while (!moved) {
			randomNum = rand.nextInt(columns);
			moved = g.placePiece(randomNum, rows - 1);
		}
	}

	/**
	 * This is where I'd put that alpha-beta pruning algorithm if given more
	 * time
	 */
	public void makeSmartMove() {

	}
}

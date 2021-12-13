package com.pennypop.project.controller;

import com.pennypop.project.Config;
import com.pennypop.project.GUI.connect4board.InternalBoard;
import com.pennypop.project.controller.heurstics.Heurstic;
import com.pennypop.project.controller.heurstics.WeightedPlacesHeurstic;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * A really really bad connect 4 AI Just makes random moves for now
 * <p>
 * I plan on adding the 'intelligence' part of AI later, but currently I'm out
 * of time This class is mostly intended to show that my Game model is
 * compatible with AI
 *
 * @author Kevin
 */
//  5
//
//
//
//

class Data {
    private int val;
    private int col;


    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Data(int col, int val) {
        this.col = col;
        this.val = val;
    }

    public Data(int val) {
        this.val = val;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}


public class AI {
    private GameLogic g;
    int k;
    Heurstic heurstic;
//    static Vector<HashMap<String, Data>> memo;
//    static boolean add = true;

    public AI(GameLogic g) {
//        if(add){
//            add = false;
//            memo = new Vector<>();
//            memo.setSize(Config.maxDepth+1);
//            for (int i = 0; i < Config.maxDepth+1; i++) {
//                memo.set(i, new HashMap<String, Data>());
//            }
//        }
        // state ( A )  after 15 , has experincae 5
        //after 3 state  , state ( A )  after 12 , has experincae 8
        this.g = g;
        this.k = Config.maxDepth;
        heurstic = new WeightedPlacesHeurstic(g.getNumColumns(), g.getNumRows());
    }


    public int decision(InternalBoard board) { // from 0  to #col
        return maximize(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0).getCol();
    }

    private Data minimize(InternalBoard board, int alpha, int beta, int currentDepth) {
        if (currentDepth > k) {
            return new Data(heurstic.evaluate(board));
        }
//        String concatonate = board.hash();
//        if (memo.get(currentDepth).containsKey(concatonate)) {
//            return memo.get(currentDepth).get(concatonate);
//        }


        int minValue = Integer.MAX_VALUE;
        int bestAction = 0;
        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.PLAYER);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            Data data = maximize(nextMoves.get(i), alpha, beta, currentDepth + 1);
            if (data.getVal() < minValue) {
                minValue = data.getVal();
                bestAction = i;
            }
            if (minValue <= alpha) {
                break;
            }
            beta = Math.min(beta, minValue);
        }
        Data ret = new Data(bestAction, minValue);
//        memo.get(currentDepth).put(concatonate, ret);
        return ret;
    }

    private Data maximize(InternalBoard board, int alpha, int beta, int currentDepth) {
        if (currentDepth > k) {
            return new Data(heurstic.evaluate(board));
        }

//        String concatonate = board.hash();
//        if (memo.get(currentDepth).containsKey(concatonate)) {
//            return memo.get(currentDepth).get(concatonate);
//        }

        int maxValue = Integer.MIN_VALUE;
        int bestAction = 0;
        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.AI);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            Data data = minimize(nextMoves.get(i), alpha, beta, currentDepth + 1);
            if (data.getVal() > maxValue) {
                maxValue = data.getVal();
                bestAction = i;
            }
            if (maxValue >= beta) {
                break;
            }
            alpha = Math.max(alpha, maxValue);
        }
        Data ret = new Data(bestAction, maxValue);
//        memo.get(currentDepth).put(concatonate, ret);
        return ret;
    }


    public void makeMove(InternalBoard board) {
        int col = decision(board);
        g.placePiece(col);
//		int rows = g.getNumRows();
//		int columns = g.getNumColumns();
//		Random rand = new Random();
//		int randomNum = rand.nextInt(columns);
//		boolean moved = false;
//		while (!moved) {
//			randomNum = rand.nextInt(columns);
//			moved = g.placePiece(randomNum, rows - 1);
//		}
    }
}

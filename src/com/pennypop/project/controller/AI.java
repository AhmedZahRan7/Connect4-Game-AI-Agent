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
    static Vector<HashMap<String, Data>> memo;
    private int rootValue = 0;

    public AI(boolean withPruning,GameLogic g) {
        memo = new Vector<>();
        memo.setSize(Config.maxDepth + 1);
        for (int i = 0; i < Config.maxDepth + 1; i++) {
            memo.set(i, new HashMap<String, Data>());
        }
        this.g = g;
        this.k = Config.maxDepth;
        heurstic = new WeightedPlacesHeurstic(g.getNumColumns(), g.getNumRows());
    }


    public int decision(InternalBoard board) { // from 0  to #col
        rootValue = heurstic.evaluate(board);
        return maximize(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0).getCol();
    }

    private Data minimize(InternalBoard board, int alpha, int beta, int currentDepth) {
        if (currentDepth >= Math.min(k, board.getEmptyCells())) {
            return new Data(heurstic.evaluate(board));
        }
        String concatonate = board.hash();
        if (memo.get(currentDepth).containsKey(concatonate)) {
            return memo.get(currentDepth).get(concatonate);
        }
        int minValue = Integer.MAX_VALUE;
        int bestAction = -1;
        int preHeoristice = Integer.MAX_VALUE;
        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.PLAYER);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            int currentHeurstic = heurstic.evaluate(nextMoves.get(i));
            Data data = maximize(nextMoves.get(i), alpha, beta, currentDepth + 1);
            if (data.getVal() < minValue) {
                minValue = data.getVal();
                bestAction = i;
                preHeoristice = currentHeurstic;
            }else if(data.getVal() == minValue && preHeoristice > currentHeurstic){
                bestAction = i ;
                preHeoristice = currentHeurstic;
            }
            if (minValue <= alpha) {
                break;
            }
            beta = Math.min(beta, minValue);
        }
        Data ret = new Data(bestAction, bestAction == -1 ? Integer.MIN_VALUE : minValue);
        memo.get(currentDepth).put(concatonate, ret);
        return ret;
    }

    private Data maximize(InternalBoard board, int alpha, int beta, int currentDepth) {
        if (currentDepth >= Math.min(k, board.getEmptyCells())) {
            return new Data(heurstic.evaluate(board));
        }
        String concatonate = board.hash();
        if (memo.get(currentDepth).containsKey(concatonate)) {
            return memo.get(currentDepth).get(concatonate);
        }

        int maxValue = Integer.MIN_VALUE;
        int bestAction = -1;
        int preHurestic = Integer.MIN_VALUE;
        int currentVal = heurstic.evaluate(board);
        if (currentVal - rootValue < Config.THRESHOLD) {
            Data ret = new Data(currentVal, maxValue);
            memo.get(currentDepth).put(concatonate, ret);
//            System.out.println("zharan is " + currentVal);
//            board.printState();
            return ret;
        }
        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.AI);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            Data data = minimize(nextMoves.get(i), alpha, beta, currentDepth + 1);
            int currentHeurstic = heurstic.evaluate(nextMoves.get(i));
            if (currentDepth == 0) {
//                System.out.println(data.getVal());
//                System.out.println(heurstic.evaluate(nextMoves.get(i)));
//                nextMoves.get(i).printState();
            }
            if (data.getVal() > maxValue) {
                maxValue = data.getVal();
                bestAction = i;
                preHurestic = currentHeurstic;
            } else if (data.getVal() == maxValue && currentHeurstic > preHurestic) {
                bestAction = i;
                preHurestic = currentHeurstic;
            }
            if (maxValue >= beta) {
                break;
            }
            alpha = Math.max(alpha, maxValue);
        }
        Data ret = new Data(bestAction, bestAction == -1 ? Integer.MAX_VALUE : maxValue);
        memo.get(currentDepth).put(concatonate, ret);
        return ret;
    }


    public void makeMove(InternalBoard board) {
        int col = decision(board);
        g.placePiece(col);
    }
}

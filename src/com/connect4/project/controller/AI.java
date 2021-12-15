package com.pennypop.project.controller;

import com.pennypop.project.Config;
import com.pennypop.project.GUI.connect4board.InternalBoard;
import com.pennypop.project.controller.heurstics.Heurstic;
import com.pennypop.project.controller.heurstics.WeightedPlacesHeurstic;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


class Data {
    private final int val;
    private int col;

    public int getVal() {
        return val;
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
}


public class AI {
    private final GameLogic gameLogic;
    int MAX_DEPTH;
    Heurstic heurstic;
    static Vector<HashMap<String, Data>> memo;
    private int rootValue = 0;
    final private boolean withPruning;
    private int numberOfExpansionNodes;

    public AI(boolean withPruning,GameLogic g) {
        this.withPruning = withPruning;
        memo = new Vector<>();
        memo.setSize(Config.maxDepth + 1);

        // initializing the memory.
        for (int i = 0; i < Config.maxDepth + 1; i++) {
            memo.set(i, new HashMap<>());
        }

        this.gameLogic = g;
        this.MAX_DEPTH = Config.maxDepth;
        // getting the heuristic, we can add more later.
        heurstic = new WeightedPlacesHeurstic(g.getNumColumns(), g.getNumRows());
    }


    public int decision(InternalBoard board) { // from 0  to #col
        this.numberOfExpansionNodes = 0;
        System.out.println("\nAgent is thinking....");
        long start = System.currentTimeMillis();
        // calculating the evaluation of the original board, to compare it later with next steps.
        rootValue = heurstic.evaluate(board);
        int step = maximize(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0).getCol();
        // logging
        System.out.println("Number of expanded nodes: " + numberOfExpansionNodes);
        System.out.println("Time agent took to make a decision: " + ((System.currentTimeMillis() - start)) + " ms");

        return step;
    }

    private Data minimize(InternalBoard board, int alpha, int beta, int currentDepth) {
        // exceeds the predetermined depth.
        if (currentDepth >= Math.min(MAX_DEPTH, board.getEmptyCells())) {
            return new Data(heurstic.evaluate(board));
        }
        // getting the hash of the board.
        String boardHash = board.hash();
        if (memo.get(currentDepth).containsKey(boardHash)) {
            return memo.get(currentDepth).get(boardHash);
        }

        int minValue = Integer.MAX_VALUE; // min answer till now
        int bestAction = -1; // action to take
        int prevHeuristic = Integer.MAX_VALUE; // to break ties.
        this.numberOfExpansionNodes++;
        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.PLAYER);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            // calculate it to choose the most promising board if there's a tie.
            int currentHeuristic = heurstic.evaluate(nextMoves.get(i));
            Data data = maximize(nextMoves.get(i), alpha, beta, currentDepth + 1);
            if (data.getVal() < minValue) {
                minValue = data.getVal();
                bestAction = i;
                prevHeuristic = currentHeuristic;
            }else if(data.getVal() == minValue && prevHeuristic > currentHeuristic){
                bestAction = i ;
                prevHeuristic = currentHeuristic;
            }
            // pruning
            if (withPruning && minValue <= alpha) {
                break;
            }
            beta = Math.min(beta, minValue);
        }
        Data ret = new Data(bestAction, bestAction == -1 ? Integer.MIN_VALUE : minValue);
        memo.get(currentDepth).put(boardHash, ret);
        return ret;
    }

    private Data maximize(InternalBoard board, int alpha, int beta, int currentDepth) {
        // exceeds the predetermined depth.
        if (currentDepth >= Math.min(MAX_DEPTH, board.getEmptyCells())) {
            return new Data(heurstic.evaluate(board));
        }
        // getting the hash of the board.
        String boardHash = board.hash();
        if (memo.get(currentDepth).containsKey(boardHash)) {
            return memo.get(currentDepth).get(boardHash);
        }

        int maxValue = Integer.MIN_VALUE; // max answer till now
        int bestAction = -1; // action to take
        int prevHeuristic = Integer.MIN_VALUE; // to break ties.
        int boardEvaluation = heurstic.evaluate(board);
        this.numberOfExpansionNodes++;

        // Comparing the board evaluation with root value, if we are doing so bad just trim the tree.
        if (withPruning && boardEvaluation - rootValue < Config.THRESHOLD) {
            Data ret = new Data(boardEvaluation, maxValue);
            memo.get(currentDepth).put(boardHash, ret);
            return ret;
        }

        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.AI);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            Data data = minimize(nextMoves.get(i), alpha, beta, currentDepth + 1);
            // calculate it to choose the most promising board if there's a tie.
            int currentHeuristic = heurstic.evaluate(nextMoves.get(i));
            if (data.getVal() > maxValue) {
                maxValue = data.getVal();
                bestAction = i;
                prevHeuristic = currentHeuristic;
            } else if (data.getVal() == maxValue && currentHeuristic > prevHeuristic) {
                bestAction = i;
                prevHeuristic = currentHeuristic;
            }
            if (withPruning && maxValue >= beta) {
                break;
            }
            alpha = Math.max(alpha, maxValue);
        }

        Data ret = new Data(bestAction, bestAction == -1 ? Integer.MAX_VALUE : maxValue);
        memo.get(currentDepth).put(boardHash, ret);
        return ret;
    }

    public void makeMove(InternalBoard board) {
        gameLogic.placePiece(decision(board));
    }
}

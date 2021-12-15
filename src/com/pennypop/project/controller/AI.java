package com.pennypop.project.controller;

import com.pennypop.project.Config;
import com.pennypop.project.GUI.connect4board.InternalBoard;
import com.pennypop.project.controller.heurstics.Heurstic;
import com.pennypop.project.controller.heurstics.WeightedPlacesHeurstic;

import java.util.ArrayList;
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
    private List<List<Integer>> trace;
    private int id = 0;
    private HashMap<Integer, Integer> evaluations;

    public AI(boolean withPruning, GameLogic g) {
        if (Config.maxDepth > 6) {
            Config.DEBUG = false;
        }
        this.withPruning = withPruning;
        memo = new Vector<>();
        memo.setSize(Config.maxDepth + 10);
        trace = new ArrayList<>();
        // initializing the memory.
        for (int i = 0; i < Config.maxDepth + 10; i++) {
            memo.set(i, new HashMap<>());
        }

        this.gameLogic = g;
        this.MAX_DEPTH = Config.maxDepth;
        // getting the heuristic, we can add more later.
        evaluations = new HashMap<>();
        heurstic = new WeightedPlacesHeurstic(g.getNumColumns(), g.getNumRows());
    }


    public int decision(InternalBoard board) { // from 0  to #col
        this.numberOfExpansionNodes = 0;
        System.out.println("\nAgent is thinking....");
        // calculating the evaluation of the original board, to compare it later with next steps.
        rootValue = heurstic.evaluate(board);
        trace.clear();
        trace = new ArrayList<>();
        if (Config.DEBUG) {
            for (int i = 0; i < 1000001; i++) {
                trace.add(new ArrayList<>());
            }
        }
        id = 0;
        long start = System.nanoTime();
        int col = maximize(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0).getCol();
        // logging
        System.out.println("Number of expanded nodes: " + numberOfExpansionNodes);
        System.out.println("Time agent took to make a decision: " + ((System.nanoTime() - start) / 1e6) + " ms");
        if (Config.DEBUG) {
            ArrayList<Integer> g = new ArrayList<>();
            print_trace(0, 0, -1, g);
        }

        return col;
    }

    private Data minimize(InternalBoard board, int alpha, int beta, int currentDepth , int current_id) {
        // exceeds the predetermined depth.
        if (currentDepth >= Math.min(MAX_DEPTH, board.getEmptyCells())) {
            Data ret = new Data(heurstic.evaluate(board));
            return cacheData(ret, current_id);
        }
        // getting the hash of the board.
        String boardHash = board.hash();
        if (memo.get(currentDepth).containsKey(boardHash)) {
            Data ret = memo.get(currentDepth).get(boardHash);
            return cacheData(ret, current_id);
        }

        int minValue = Integer.MAX_VALUE; // min answer till now
        int bestAction = -1; // action to take
        int prevHeuristic = Integer.MAX_VALUE; // to break ties.
        this.numberOfExpansionNodes++;
        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.PLAYER);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            int nx = ++id;
            if (Config.DEBUG) {
                trace.get(current_id).add(nx);
            }
            // calculate it to choose the most promising board if there's a tie.
            int currentHeuristic = heurstic.evaluate(nextMoves.get(i));
            Data data = maximize(nextMoves.get(i), alpha, beta, currentDepth + 1,nx);
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
        return cacheData(ret, current_id);
    }
    private Data cacheData(Data data, int current_id) {
        evaluations.put(current_id, data.getVal()); // for drawing the tree
        return data;
    }

    private Data maximize(InternalBoard board, int alpha, int beta, int currentDepth, int current_id) {
        // exceeds the predetermined depth.
        if (currentDepth >= Math.min(MAX_DEPTH, board.getEmptyCells())) {
            return cacheData(new Data(heurstic.evaluate(board)), current_id);
        }
        // getting the hash of the board.
        String boardHash = board.hash();
        if (memo.get(currentDepth).containsKey(boardHash)) {
            return cacheData(memo.get(currentDepth).get(boardHash), current_id);
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
            return cacheData(ret, current_id);
        }

        List<InternalBoard> nextMoves = board.getNextMoves(InternalBoard.AI);
        for (int i = 0; i < nextMoves.size(); i++) {
            if (nextMoves.get(i) == null) continue;
            int nx = ++id;
            if (Config.DEBUG) {
                trace.get(current_id).add(nx);
            }
            Data data = minimize(nextMoves.get(i), alpha, beta, currentDepth + 1,nx);
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
        return cacheData(ret, current_id);
    }

    public void makeMove(InternalBoard board) {
        gameLogic.placePiece(decision(board));
    }

    int get_len(int num) {
        if (num == 0) {
            return 1;
        }
        int ans = 0;
        if (num < 0) {
            ans++;
            num *= -1;
        }
        while (num > 0) {
            ans++;
            num /= 10;
        }
        return ans;
    }

    ArrayList<Integer> clone(List<Integer> g) {
        return new ArrayList<>(g);
    }

    int get_last(List<Integer> g) {
        int last = 0;
        for (int nx : g) {
            last = nx;
        }
        return last;
    }

    void print_trace(int root, int level, int praent, ArrayList<Integer> sp) {
        int len = 0;
        if (root != 0) {
            System.out.print("--->");
            len += 4;
        }else {
            System.out.println();
            System.out.println();
        }
        System.out.print(evaluations.get(root));
        len += get_len(evaluations.get(root));
        len += get_len(root);
        len += get_len(praent);
        System.out.print("(");
        System.out.print(root);
        System.out.print(")");
        System.out.print("(");
        System.out.print(praent);
        System.out.print(")");
        len += 4;
        int cur = 0;
        int last = get_last(sp);
        for (int nx : trace.get(root)) {
            ArrayList<Integer> temp = clone(sp);
            temp.add(level + len);
            print_trace(nx, level + len, root, temp);
            if (cur == trace.get(root).size() - 1) continue;
            for (int i = 0; i < last + len + 1; i++) {
                if (sp.contains(i) || i == level + len) {
                    System.out.print("|");
                } else System.out.print(" ");
            }
            cur++;
        }
        if (trace.get(root).size() == 0) {
            System.out.println();
        }
    }
}
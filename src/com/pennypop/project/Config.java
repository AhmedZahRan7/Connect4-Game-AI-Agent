package com.pennypop.project;

public class Config {
    public enum Player{
        PLAYER,
        AI,
        EMPTY,
    }
    public static int THRESHOLD = -500;
    public static int maxDepth = 8;
    public static boolean DEBUG = true;
}

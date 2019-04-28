package model;

public class Scores {

    public static int ENEMIES_KILLED;
    public static int ITEMS_COLLECTED;
    public static int TIME_TAKEN;
    public static boolean VICTORY;

    public static void resetScores(){
        ENEMIES_KILLED = 0;
        ITEMS_COLLECTED = 0;
        TIME_TAKEN = 0;
        VICTORY = false;
    }
}

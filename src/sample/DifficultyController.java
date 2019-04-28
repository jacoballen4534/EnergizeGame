package sample;

import java.util.ArrayList;

public class DifficultyController {

    public static class IntObj {
        public int value;
    }

    //Parameters for controlling difficulty
    public static IntObj GRUNT_HEALTH = new IntObj();
    public static IntObj GRUNT_DAMAGE = new IntObj();
    public static IntObj PLAYER_HEALTH = new IntObj();
    public static IntObj PLAYER_DAMAGE = new IntObj();
    public static IntObj PICKUP_REFILL_AMOUNT = new IntObj();
    public static IntObj FIRE_SCROLL_DAMAGE = new IntObj();
    public static IntObj ICE_SCROLL_DURATION = new IntObj();
    public static IntObj BOSS_HEALTH = new IntObj();
    public static IntObj BOSS_BASE_DAMAGE = new IntObj();
    public static IntObj BOSS_SPECIAL_DAMAGE = new IntObj();

    public static String difficulty;

    private static ArrayList<IntObj> params = new ArrayList<IntObj>() {
        {
            add(GRUNT_HEALTH);
            add(GRUNT_DAMAGE);
            add(PLAYER_HEALTH);
            add(PLAYER_DAMAGE);
            add(PICKUP_REFILL_AMOUNT);
            add(FIRE_SCROLL_DAMAGE);
            add(ICE_SCROLL_DURATION);
            add(BOSS_HEALTH);
            add(BOSS_BASE_DAMAGE);
            add(BOSS_SPECIAL_DAMAGE);
        }
    };

    private static int[][] values = {
            {50,5,200,50,100,50,7000,150,10,30}, //Easy
            {100,10,100,35,40,80,5000,170,15,40}, //Normal
            {150,20,75,25,25,75,3000,200,25,50} //Hard
    };

    private static void updateDifficulty(int difficulty){
        for (int i=0;i<9;i++) {
            params.get(i).value =  values[difficulty][i];
        }
    }

    public static void setDifficulty(String newDifficulty){
        difficulty = newDifficulty;
        switch(newDifficulty){
            case "EASY":
                updateDifficulty(0);
                break;
            case "NORMAL":
                updateDifficulty(1);
                break;
            case "HARD":
                updateDifficulty(2);
                break;
            default:
                break;
        }
    }

}

package sample;

public class DifficultyController {

    //Parameters
    public static Integer GRUNT_HEALTH;
    public static Integer GRUNT_DAMAGE;
    public static Integer PLAYER_HEALTH;
    public static Integer PLAYER_DAMAGE;
    public static Integer PICKUP_REFILL_AMOUNT;
    public static Integer FIRE_SCROLL_DAMAGE;
    public static Integer ICE_SCROLL_DURATION;
    public static Integer BOSS_HEALTH;
    public static Integer BOSS_DAMAGE;

    private static Integer[] params = {GRUNT_HEALTH,GRUNT_DAMAGE,PLAYER_HEALTH,PLAYER_DAMAGE,
            PICKUP_REFILL_AMOUNT,FIRE_SCROLL_DAMAGE,ICE_SCROLL_DURATION,BOSS_HEALTH,BOSS_DAMAGE};

    private static int[][] values = {
            {50,5,200,50,100,50,7,150,20}, //Easy
            {100,10,100,35,40,80,5,170,25}, //Normal
            {150,20,75,25,25,75,3,200,40} //Hard
    };

    private static void updateDifficulty(int difficulty){
        for (int i=0;i<9;i++) {
            params[i] = new Integer(values[difficulty][i]);
        }
        System.out.println(GRUNT_HEALTH);
    }

    public static void setDifficulty(String difficulty){
        switch(difficulty){
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

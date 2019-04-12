package model;

import sample.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Map {
    //Holds all the levels.
    private HashMap<Integer, Level> levels = new HashMap<>();
    //May need to put this into a level class so we dont need to get the floor each time.
    private Level currentLevel;
    Game game; //To add the protagonist to.
    private final int NumberOfLevles = 3;
    private int tutorialRow;
    private final int MAP_WIDTH = 10;
    private final int MAP_HEIGHT = 10;
    //Hold the layout of the different levels that makes up the map, Can be used for mini-map.
    //Using bitwise operations to indicate which side shares a wall with another level (need a door). Or 0 if it is not a level
    // 4 bits in the form Top,Right,Bottom,Left.
    private ArrayList<ArrayList<Integer>> levelLayout = new ArrayList<>();

    public Map(Game game){
        this.game = game;
        this.generateLevelLayout(MAP_HEIGHT, MAP_WIDTH,15,4);

        for (int row = 0; row < levelLayout.size(); row ++) {
            for (int col = 0; col < levelLayout.get(row).size(); col++) {
                System.out.print(levelLayout.get(row).get(col) + "\t");
            }
            System.out.println("");
        }




        for (int row = 0; row < levelLayout.size(); row ++) {
            for (int col = 1; col < levelLayout.get(row).size(); col++) {
                if (!levelLayout.get(row).get(col).equals(0)) {
                    this.currentLevel = new Level(row, col, MAP_WIDTH, levelLayout.get(row).get(col)); //Set to current level to garnette there is something to load
                    this.levels.put(col + row * MAP_WIDTH, this.currentLevel);
                }
            }
        }

        this.currentLevel = new Level(PreLoadedImages.tutorialRoom, tutorialRow,0 ,MAP_WIDTH); //Level number = col + row * width, col is 0
        this.levels.put(tutorialRow*MAP_WIDTH, this.currentLevel); //Level number = col + row * width, col is 0


//        Load tutorial room and add it to start of levels array.
//        Go through and make each level

    }

    public Level getLevel(int levelNumber) {
        if (this.levels.size() >= levelNumber) {
            //By only updating currentLevelImage, this will ensure if a non existent level is requested it will still return a valid level
            this.currentLevel = this.levels.get(levelNumber);
        }
        return this.currentLevel;
    }

    public void loadLevel(int levelNumber) {
        if (this.levels.containsKey(levelNumber)) {
            this.currentLevel = this.levels.get(levelNumber);
        }
        this.currentLevel.loadLevel(game);//Pass the game in to add the protagonist to it.
    }

    public void loadLevel() {
        this.currentLevel.loadLevel(game);//Pass the game in to add the protagonist to it.
    }

    public int getCurrentLevelNumber() {
        return this.currentLevel.getLevelNumber();
    }

    public int getCurrentLevelHeight() {
        return this.currentLevel.getLevelHeight();
    }

    public int getCurrentLevelWidth() {
        return this.currentLevel.getLevelWidth();
    }

    private void generateLevelLayout(int rows, int cols, int maxTunnels, int maxTunnelLength) {
        ArrayList<ArrayList<Integer>> directions = new ArrayList<>(); //A list of directions to randomly pick from. form x,y
        directions.add(new ArrayList<>(Arrays.asList(-1, 0))); //left
        directions.add(new ArrayList<>(Arrays.asList(1, 0))); //right
        directions.add(new ArrayList<>(Arrays.asList(0, -1))); //up
        directions.add(new ArrayList<>(Arrays.asList(0, 1))); //down

        int currentRow = Game.getNextRandomInt(rows) + 1; //Pick a random starting height for the tutorial room
        this.tutorialRow = currentRow - 1; //Return this to know where the tutorial room is.
        int currentCol = 2;
        int randomTunnelLength;
        int currentTunnelLength;
        ArrayList<Integer> lastDirection = directions.get(2); //Initialize us pointing up so the next direction will have to be to the right, as this is how we leave the tutorial room
        ArrayList<Integer> randomDirection;
        ArrayList<ArrayList<Integer>> levelBuilder = new ArrayList<>(); //1 indicates a room, 0 is empty

//        Initialize the map with no rooms
        for (int row = 0; row < rows + 2; row++) { //Add a 1 row buffer above and below for easier boundary checks later.
            ArrayList<Integer> column = new ArrayList<>();
            for (int col = 0; col < cols + 2; col++) { //Add 1 col buffer for tutorial room to be on its own, then another border buffer.
                if (col == 1 && row == currentRow) {
                    column.add(1); //Put the tutorial room in the first column at random height.
                } else {
                    column.add(0);
                }
            }
            levelBuilder.add(column);
        }

        while (maxTunnels > 0) {

            //Make sure you dont keep going in the same direction as you just went, and dont backtrack over an existing path
            do {
                randomDirection = directions.get(Game.getNextRandomInt(4)); //Pick a new direction
            }
            while (randomDirection.get(0).equals(lastDirection.get(0) * -1) && randomDirection.get(1).equals(lastDirection.get(1) * -1) ||
                    randomDirection.get(0).equals(lastDirection.get(0)) && randomDirection.get(1).equals(lastDirection.get(1)));

            randomTunnelLength = Game.getNextRandomInt(maxTunnelLength) + 1; //Add 1 as it was from 0<length.
            currentTunnelLength = 0;

            while (currentTunnelLength < randomTunnelLength) { //add floors until we reach the desired length, or hit an edge.

                //Check if we are on the boundary and trying to go offscreen. Use Col = 2 as bounday so the tutoial room can only have an exit on its right
                if (currentRow == 1 && randomDirection.get(1).equals(-1) || currentCol == 2 && randomDirection.get(0).equals(-1) ||
                        currentRow == rows - 2 && randomDirection.get(1).equals(1) ||
                        currentCol == cols - 1 && randomDirection.get(0).equals(-1)) {
                    break;
                } else {
                    levelBuilder.get(currentRow).set(currentCol, 1); //If this is not out of bounds, set it to a level.
                    currentRow += randomDirection.get(1);
                    currentCol += randomDirection.get(0);
                    currentTunnelLength++;
                }
            }

            if (currentTunnelLength > 0) { //Make sure we added some levels to this tunnel.
                lastDirection = randomDirection;
                maxTunnels--;
            }
        }

//        pass over level layout and determine what type of level it is, Then put it into levelLayout
        this.levelLayout.clear();

        ////////////////FOR DEBUG - Draw map //////////////////////
        for (int row = 0; row < levelBuilder.size(); row ++) {
            for (int col = 0; col < levelBuilder.get(row).size(); col++) {
                System.out.print(levelBuilder.get(row).get(col));
            }
            System.out.println("");
        }
        ///////////////////////////////////////////////////////////


        for (int row = 1; row < levelBuilder.size() - 1; row ++) {
            ArrayList<Integer> column = new ArrayList<>();
            for (int col = 1; col < levelBuilder.get(row).size() - 1; col++) {
                int doorsForLevel = 0b0000; //initialize with no doors. This is the same as there not being a level
                //Dont need to check for out of bounds as we added a buffer of 0's
                //Checking if there are levels around the current level, if there are add a door to connect them
                if (levelBuilder.get(row).get(col).equals(1)) { //Only check neighbors if it is a level itself.
                    if (levelBuilder.get(row - 1).get(col).equals(1)) { //Check above
                        doorsForLevel |= 0b1000;
                    }
                    if (levelBuilder.get(row).get(col + 1).equals(1)) { //Check right
                        doorsForLevel |= 0b0100;
                    }
                    if (levelBuilder.get(row + 1).get(col).equals(1)) { //Check below
                        doorsForLevel |= 0b0010;
                    }
                    if (levelBuilder.get(row).get(col - 1).equals(1)) { //Check left
                        doorsForLevel |= 0b0001;
                    }
                }
                column.add(doorsForLevel);
            }
            this.levelLayout.add(column);
        }
    }
}

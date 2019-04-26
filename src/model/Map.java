package model;

import Multiplayer.Server;
import javafx.geometry.Point2D;
import sample.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;


public class Map {
    //Holds all the levels.
    private TreeMap<Integer, Level> levels = new TreeMap<>();
    //May need to put this into a level class so we dont need to get the floor each time.
    private Level currentLevel;
    private Game game; //To add the protagonist to.
    private int tutorialRow;
    private final int MAP_WIDTH = 10;
    private final int MAP_HEIGHT = 11;
    private final int LEVEL_WIDTH = 20;
    private final int LEVEL_HEIGHT = 15; //Cannot be less than 12 as the tutorial room door needs to align
    private ArrayList<ArrayList<TreeMap<TileType, Point2D>>> allDoorLocations = new ArrayList<>(); //2d array list of door locations. in col,row form
    private static Level bossLevel; //This has level number of 8055
    private int bossEntranceLevelNumber;// This is the room that goes to boss level.
    private int tutorialLevelNumber;

    //Hold the layout of the different levels that makes up the map, Can be used for mini-map.
    //Using bitwise operations to indicate which side shares a wall with another level (need a door). Or 0 if it is not a level
    // 4 bits in the form Top,Right,Bottom,Left.
    private ArrayList<ArrayList<Boolean>> levelLayout = new ArrayList<>();


    public Map(Game game, long MAP_SEED){
        this.game = game;
        Random randomGenerator = new Random(MAP_SEED);

        //Generate a random layout of levels.
        this.generateLevelLayout(MAP_HEIGHT, MAP_WIDTH,15,4, randomGenerator);
        //Generate tutorial room first as the shortest path takes a while so start it first
        this.tutorialLevelNumber = tutorialRow * MAP_WIDTH;//Level number = col + row * width, where col is 0
        this.currentLevel = new Level(PreLoadedImages.tutorialRoom, tutorialLevelNumber,MAP_WIDTH, false);
        this.levels.put(tutorialLevelNumber, this.currentLevel); //Level number = col + row * width, col is 0
        bossLevel = new Level(PreLoadedImages.bossRoom, 8055, MAP_WIDTH, true);
        this.levels.put(8055, bossLevel);

        //For each level in the map, generate a random level.
        for (int row = 0; row < MAP_HEIGHT; row ++) {
            for (int col = 1; col < MAP_WIDTH; col++) {
                if (levelLayout.get(row).get(col)) { //Make sure there is a level in this location
                    final int levelNumber = col + row * MAP_WIDTH;
                    this.levels.put (levelNumber, new Level(levelNumber, MAP_WIDTH, this.allDoorLocations.get(row).get(col), LEVEL_WIDTH, LEVEL_HEIGHT, randomGenerator)); //Set to current level to garnette there is something to load
                }
            }
        }

        this.levels.get(bossEntranceLevelNumber).setBossEntrance(); //Indicate that this is the boss level entrance so the door goes to 8055

    }

    public void removeObject(GameObject toRemove) {
        this.game.getProtagonist().sendToServer(Server.PACKET_REMOVE + toRemove.getSpawnID().getKey() + "," + toRemove.getSpawnID().getValue() + Server.PACKET_END); // /rm/level#,location#/e/
        levels.get(toRemove.getSpawnID().getKey()).removeObject(toRemove.getSpawnID().getValue());
    }

    public void removeObject(int level, int location) { //This gets called by the client when removing based off server. So dont need to tell the server again
        levels.get(level).removeObject(location);
    }

    public Level getLevel(int levelNumber) {
        if (this.levels.size() >= levelNumber) {
            //By only updating currentLevelImage, this will ensure if a non existent level is requested it will still return a valid level
            this.currentLevel = this.levels.get(levelNumber);
        }
        return this.currentLevel;
    }

    public int getTutorialLevelNumber() {
        return this.tutorialLevelNumber;
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

    public Level getCurrentLevel () {
        return this.currentLevel;
    }
    public int getCurrentLevelHeight() {
        return this.currentLevel.getLevelHeight();
    }

    public int getCurrentLevelWidth() {
        return this.currentLevel.getLevelWidth();
    }

    //This generates where all the different levels will be and how they connect
    private void generateLevelLayout(int rows, int cols, int maxTunnels, int maxTunnelLength, Random randomGenerator) {
        ArrayList<Point2D> directions = new ArrayList<>(); //A list of directions to randomly pick from. form x,y
        directions.add(new Point2D(-1, 0)); //left
        directions.add(new Point2D(1, 0)); //right
        directions.add(new Point2D(0, -1)); //up
        directions.add(new Point2D(0, 1)); //down

        this.levelLayout.clear();

        int currentRow = (int)(randomGenerator.nextDouble() * rows); //Pick a random starting height for the tutorial room, 1 up from the bottom and 1 down from the top.
        this.tutorialRow = currentRow; //Return this to know where the tutorial room is.
        int currentCol = 1; //This is where the first room after tutorial room is.
        int randomTunnelLength;
        int currentTunnelLength;
        int lastDirectionIndex = 2; //Initialize us pointing up so the next direction will have to be to the right, as this is how we leave the tutorial room
        int nextDirectionIndex;

//      Initialize the map with no rooms apart from tutorial room
        for (int row = 0; row < rows; row++) {
            ArrayList<Boolean> column = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                if (col == 0 && row == currentRow) {
                    column.add(true); //Put the tutorial room in the first column at random height.
                } else {
                    column.add(false);
                }
            }
            levelLayout.add(column);
        }

        while (maxTunnels > 0) {

            //Make sure you dont keep going in the same direction as you just went, and dont backtrack over an existing path
            if (lastDirectionIndex < 2) { //Last direction was vertical so pick random horizontal
                nextDirectionIndex = randomGenerator.nextBoolean() ? 2 : 3;
            } else {
                nextDirectionIndex = randomGenerator.nextBoolean() ? 0 : 1;
            }

            randomTunnelLength = (int)(randomGenerator.nextDouble() * maxTunnelLength) + 1; //Add 1 as it was from 0<length.
            currentTunnelLength = 0;

            while (currentTunnelLength < randomTunnelLength) { //add floors until we reach the desired length, or hit an edge.

                //Check if we are on the boundary and trying to go offscreen. Use Col = 1 as boundary so the tutorial room is the only room in col 0 with exit on its right
                if (currentRow == 0 && nextDirectionIndex == 2 || currentCol == 1 && nextDirectionIndex == 0  ||
                        currentRow == rows - 1 && nextDirectionIndex == 3 || currentCol == cols - 1 && nextDirectionIndex == 1 ) {
                    break;
                } else {
//                    System.out.println("row: " + currentRow + " col: " + currentCol);
                    levelLayout.get(currentRow).set(currentCol, true); //If this is not out of bounds, set it to a level.
                    currentRow += directions.get(nextDirectionIndex).getY();
                    currentCol += directions.get(nextDirectionIndex).getX();
                    currentTunnelLength++;
                }
            }

            if (currentTunnelLength > 0) { //Make sure we added some levels to this tunnel.
                lastDirectionIndex = nextDirectionIndex;
                maxTunnels--;
            }
        }

        ////////////////FOR DEBUG - Draw map //////////////////////
        for (int row = 0; row < rows; row ++) {
            for (int col = 0; col < cols; col++) {
                System.out.print(levelLayout.get(row).get(col) ? 1 : 0);
            }
            System.out.println();
        }
        System.out.println();
        ///////////////////////////////////////////////////////////


        ///////////////// Add connecting doors /////////////////////
        for (int row = 0; row < rows; row ++) {
            ArrayList<TreeMap<TileType, Point2D>> doorLocationsRow = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                TreeMap<TileType, Point2D> currentLevelDoors = new TreeMap<>();
                //Checking if there are levels around the current level, if there are add a door to connect them
                if (levelLayout.get(row).get(col)) { //Only check neighbors if it is a level itself.

                    if (row > 0 && levelLayout.get(row - 1).get(col)) { //Check above, Align door
                        double adjacentDoorAboveX = allDoorLocations.get(row - 1).get(col).get(TileType.DOOR_DOWN).getX(); //Get the bottom door from room above
                        currentLevelDoors.put(TileType.DOOR_UP, new Point2D(adjacentDoorAboveX, 0));
                    }

                    if (col < cols-1 && levelLayout.get(row).get(col + 1)) { //Check right. Random door
                        //Door cant be in the top 2 or bottom 3 rows
                        int rightDoorY = (int)(randomGenerator.nextDouble() * (LEVEL_HEIGHT - 5)) + 2;
                        currentLevelDoors.put(TileType.DOOR_RIGHT, new Point2D(LEVEL_WIDTH - 1, rightDoorY));
                    }

                    if (row < rows - 1 && levelLayout.get(row + 1).get(col)) { //Check below. Random door
                        int BottomDoorX = (int)(randomGenerator.nextDouble() * (LEVEL_WIDTH - 2)) + 1;
                        currentLevelDoors.put(TileType.DOOR_DOWN, new Point2D(BottomDoorX, LEVEL_HEIGHT - 2));
                    }

                    if (col > 0 && levelLayout.get(row).get(col - 1)) { //Check left. Align door
                        double adjacentDoorLeftY = doorLocationsRow.get(col - 1).get(TileType.DOOR_RIGHT).getY(); //Get the bottom door from room above
                        currentLevelDoors.put(TileType.DOOR_LEFT, new Point2D(0, adjacentDoorLeftY));
                    }
                }
                doorLocationsRow.add(currentLevelDoors);
            }
            this.allDoorLocations.add(doorLocationsRow);
        }
        //Add a door to the highest room the furthest to the right, to go to the boss room.
        for (int col = cols-1; col >= 0; col --) {
            for (int row = 0; row < rows; row ++) {
                if (levelLayout.get(row).get(col)) { //This will enter at he highest row on the furthest col with a room in it.
                    this.bossEntranceLevelNumber = col + row * this.MAP_WIDTH;
                    int rightDoorY = (int)(randomGenerator.nextDouble() * (LEVEL_HEIGHT - 5)) + 2;
                    this.allDoorLocations.get(row).get(col).put(TileType.DOOR_RIGHT,  new Point2D(LEVEL_WIDTH - 1, rightDoorY));
                    return;
                }
            }
        }
    }

    public static Level getBossLevel() {
        return bossLevel;
    }
}

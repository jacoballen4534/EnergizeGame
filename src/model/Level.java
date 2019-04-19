package model;

import javafx.geometry.Point2D;
import sample.Game;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Map;

//These are the different things that can be on the map
enum TileType {
    FLOOR,
    WALL,
    CAMP_FIRE,
    PROTAGONIST,
    DOOR_UP,
    DOOR_RIGHT,
    DOOR_DOWN,
    DOOR_LEFT,
    ENEMY,
    NULLTILE,
    ITEM,
    GRUNT,
    BOMBER,
    ARCHER
}

public class Level {
    private ArrayList<ArrayList<TileType>> tiles = new ArrayList<>();
    private int levelNumber; //Used for doors, to go to the correct room
    private int levelWidth;
    private int levelHeight;
    private int mapWidth;//Used to calculate the level number of neighboring levels
    private TreeMap<TileType, Point2D> doorMap; //Store the doors and their location.
    //Make sure all the doors in the level are reachable. Default true and set to false later if that door exists
    private boolean topDoorReachable = true, rightDoorReachable = true, bottomDoorReachable = true, leftDoorReachable = true;
    private Point2D currentPoint;
    private Point2D nextPoint;
    private int nextXDirection, nextYDirection; //Used to be able to keep doing in the same direction
    private ArrayList<Point2D> floorLocation = new ArrayList<>(); //Keep track of all floor location placed. Everything else must be door or wall
    private final Random randomGenerator;
    private double numberOfRandomBools = 0;

    //////////////Macros, Actual size of different sprites///////////
    private final int Tile_SPRITE_WIDTH = 32;
    private final int Tile_SPRITE_HEIGHT = 32;

    private final int PROTAGONIST_SPRITE_WIDTH = 400;
    private final int PROTAGONIST_SPRITE_HEIGHT = 296;
    //Protagonist is downscaled from x8 spreadsheet to wanted size.
    private final double PROTAGONIST_SPRITE_SCALE = 0.35;

    private final int CAMP_FIRE_SPRITE_WIDTH = 64;
    private final int CAMP_FIRE_SPRITE_HEIGHT = 64;

    private final int DOOR_SPRITE_WIDTH = 70;
    private final int DOOR_SPRITE_HEIGHT = 80;
    private final int DOOR_RENDER_WIDTH = 64;
    private final int DOOR_RENDER_HEIGHT = 90;

    private final int GRUNT_SPRITE_WIDTH = 129;
    private final int GRUNT_SPRITE_HEIGHT = 111;



    public Level(BufferedImage image, int levelNumber, int mapWidth) { //Makes a level from an image
        this.levelNumber = levelNumber;
        this.randomGenerator = new Random(0); //Unused but set to allow final declaration
        this.doorMap = new TreeMap<>();
        this.mapWidth = mapWidth;
        ProcessImage(image);
    }


    //Makes a random level. Needs the current row, col and map width to find what level each door should map to
    public Level(int levelNumber, int mapWidth, TreeMap<TileType, Point2D> doorMap, int levelWidth, int levelHeight, float baseSeed) { //Pass in top and left door, if they exist
        //////////////////////////////////// DOOR SETUP /////////////////////////////////////////////////////////////
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.levelNumber = levelNumber;
        this.doorMap = doorMap;
        this.mapWidth = mapWidth;
        System.out.println("Level: " + levelNumber + "\tSeed: " + (long)(baseSeed * this.levelNumber));
        this.randomGenerator = new Random((long)(baseSeed * this.levelNumber));


        //////////////////////////////////// INITIALIZE WALLS ///////////////////////////////////////////////////////
        for (int row = 0; row < this.levelHeight; row ++) {
            ArrayList<TileType> column = new ArrayList<>();
            for (int col = 0; col < this.levelWidth; col++) {
                column.add(TileType.WALL);
            }
            this.tiles.add(column);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////// PICK STARTING POINT FOR RANDOM WALK /////////////////////////////////////////
        for (Map.Entry<TileType, Point2D> door : this.doorMap.entrySet()) {
            //Set currentPoint each time through loop to ensure it gets set at least once.
            switch (door.getKey()){
                case DOOR_UP:
                    this.topDoorReachable = false;
                    this.currentPoint = new Point2D(door.getValue().getX(), door.getValue().getY() + 1);
                    break;
                case DOOR_RIGHT:
                    this.rightDoorReachable = false;
                    this.currentPoint = new Point2D(door.getValue().getX() - 1, door.getValue().getY());
                    break;
                case DOOR_DOWN:
                    this.currentPoint = new Point2D(door.getValue().getX(), door.getValue().getY() - 2);
                    this.bottomDoorReachable = false;
                    break;
                case DOOR_LEFT:
                    this.leftDoorReachable = false;
                    this.currentPoint = new Point2D(door.getValue().getX() + 1, door.getValue().getY());
                    break;
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////

        while (this.floorLocation.size() < (this.levelWidth * this.levelHeight * 0.5)) { //Set the percentage of walls to be removed

            //place floor and add to floor list
            this.placeFloor(currentPoint, true);

            //check if a door is now reachable - update reachable boolean accordingly
            this.updateReachableDoors(currentPoint);

            //Pick next location
            this.numberOfRandomBools += 2;
            this.currentPoint = this.nextLocation(this.currentPoint, randomGenerator);
        }


        //If a door is still unreachable, make a path to it.

        //Converge from each door towards the centre. Converging at the same time will allow different paths to connect, making the full map connected earlie.
        for (int steps = 1; steps < this.levelHeight - 4; steps++) {
            if (!this.topDoorReachable) {
                this.placeFloor(new Point2D(doorMap.get(TileType.DOOR_UP).getX(), doorMap.get(TileType.DOOR_UP).getY() + steps), false);
            }
            if (!this.bottomDoorReachable) {
                this.placeFloor(new Point2D(doorMap.get(TileType.DOOR_DOWN).getX(), doorMap.get(TileType.DOOR_DOWN).getY() - steps - 1), true);
            }
        }

        for (int steps = 1; steps < this.levelWidth - 3; steps++) {
            if (!this.leftDoorReachable) {
                this.placeFloor(new Point2D(doorMap.get(TileType.DOOR_LEFT).getX() + steps, doorMap.get(TileType.DOOR_LEFT).getY()), true);
            }
            if (!this.rightDoorReachable) {
                this.placeFloor(new Point2D(doorMap.get(TileType.DOOR_RIGHT).getX() - steps, doorMap.get(TileType.DOOR_RIGHT).getY()), true);
            }
        }


        ///////////////////////////////////////// PLACE DOORS /////////////////////////////////////////////////////
        //Add all the doors to the tile list.
        for (Map.Entry<TileType, Point2D> door : this.doorMap.entrySet()) {
            this.tiles.get((int)door.getValue().getY()).set((int)door.getValue().getX(), door.getKey());
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
//        this.debugDrawFloor();
    }

    private void findPaths() {

    }



    public void debugDrawFloor() {
        System.out.println("\n\nLevel number: " + this.levelNumber + " Number of random bools: " + numberOfRandomBools);

        System.out.println("--------------------");

        int numberOfFloows = 0;
        for (int row = 0; row < this.levelHeight; row ++) {
            for (int col = 0; col < this.levelWidth; col++) {
                switch (this.tiles.get(row).get(col)) {
                    case WALL:
                        System.out.print((char)27 + "[31m" + "W");
                        break;
                    case FLOOR:
                        System.out.print((char)27 + "[0m" + "F");
                        numberOfFloows++;
                        break;
                    default:
                        System.out.print((char)27 + "[33m" + "D");
                        break;
                }
            }
            System.out.println("");
        }
        System.out.println("Number of floors: " + numberOfFloows);
    }

    //With chance of keeping direction
    private Point2D nextLocation (Point2D currentPoint, Random nextLocationRandomGenerator) {

        //Pick a random direction to step.
        int odds = (int)(nextLocationRandomGenerator.nextDouble() *  4);

        switch (odds) {
            case 0: //25 %chance of moving in x direction
                if (nextLocationRandomGenerator.nextBoolean()) {
                    this.nextXDirection = 1;
                } else {
                    this.nextXDirection = -1;
                }
                nextYDirection = 0; //To not move diagonally.
                break;
            case 1: //25% chance of moving in y direction
                if (nextLocationRandomGenerator.nextBoolean()) {
                    this.nextYDirection = 1;
                } else {
                    this.nextYDirection = -1;
                }
                nextXDirection = 0; //To not move diagonally.

                break;
                // 50% chance of staying in the same direction
        }

        Point2D nextPosition = new Point2D(currentPoint.getX() + nextXDirection, currentPoint.getY() + nextYDirection);

        //If this new position is out of bounds, move back in the opposite direction 2 steps
        if (nextPosition.getX() < 1 || nextPosition.getX() > this.levelWidth - 2 ||
                 nextPosition.getY() < 1 || nextPosition.getY() > this.levelHeight - 3) {
            nextPosition = new Point2D(currentPoint.getX() - nextXDirection, currentPoint.getY() - nextYDirection);
        }
        
        return nextPosition;
    }

    /*private Point2D nextLocation (Point2D currentPoint, Random randomGenerator) {

        if (randomGenerator) {
            this.nextXDirection = increase ? 1 : -1;
            nextYDirection = 0; //To not move diagonally.
        } else {
            this.nextYDirection = increase ? 1 : -2;
            nextXDirection = 0; //To not move diagonally.
        }

        Point2D nextPosition = new Point2D(currentPoint.getX() + nextXDirection, currentPoint.getY() + nextYDirection);

        //If this new position is out of bounds, move back in the opposite direction 2 steps
        if (nextPosition.getX() < 1 || nextPosition.getX() > this.levelWidth - 2 ||
                nextPosition.getY() < 1 || nextPosition.getY() > this.levelHeight - 3) {
            nextPosition = new Point2D(currentPoint.getX() - nextXDirection, currentPoint.getY() - nextYDirection);
        }
        return nextPosition;
    }*/



    private void placeFloor(Point2D location, boolean doubleTile) { //Double tile indicates if the tile underneath should be added also.
        // This is sometimes needed due the protagonist being 2 tiles tall
        int row = (int)location.getY();
        int col = (int)location.getX();

        if (this.tiles.get(row).get(col) != TileType.FLOOR) { //Only place floor if it is not already a floor.
            this.floorLocation.add(new Point2D(col, row));
        }
        this.tiles.get(row).set(col, TileType.FLOOR);

        if (doubleTile && this.tiles.get(row + 1).get(col) != TileType.FLOOR) {
            this.tiles.get(row + 1).set(col, TileType.FLOOR);
            this.floorLocation.add(new Point2D(col, row + 1));
        }
    }

    private void updateReachableDoors(Point2D newLocation) {
        //Only update doors that are still unreachable.
        //Check if the new location is next to a door
        if (!this.topDoorReachable) {
            this.topDoorReachable = (newLocation.getX() == this.doorMap.get(TileType.DOOR_UP).getX() &&
                    newLocation.getY() == this.doorMap.get(TileType.DOOR_UP).getY() + 1);
        }
        if (!this.rightDoorReachable) {
            this.rightDoorReachable = (newLocation.getX() == this.doorMap.get(TileType.DOOR_RIGHT).getX() - 1 &&
                    newLocation.getY() == this.doorMap.get(TileType.DOOR_RIGHT).getY());
        }
        if (!this.bottomDoorReachable) {
             this.bottomDoorReachable = (newLocation.getX() == this.doorMap.get(TileType.DOOR_DOWN).getX() &&
                    newLocation.getY() == this.doorMap.get(TileType.DOOR_DOWN).getY() - 2);
        }
        if (!this.leftDoorReachable) {
             this.leftDoorReachable = (newLocation.getX() == this.doorMap.get(TileType.DOOR_LEFT).getX() + 1 &&
                    newLocation.getY() == this.doorMap.get(TileType.DOOR_LEFT).getY());
        }
    }


    public ArrayList<ArrayList<TileType>> getTiles() {
        return this.tiles;
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    public int getLevelWidth() {
        return this.levelWidth;
    }

    public int getLevelHeight() {
        return this.levelHeight;
    }

    public TreeMap<TileType,Point2D> getDoors () {
        return this.doorMap;
    }

    private void ProcessImage(BufferedImage image) {
        this.levelWidth = image.getWidth();
        this.levelHeight = image.getHeight();
        for (int row = 0; row < this.levelHeight; row++) {
            ArrayList<TileType> column = new ArrayList<>();
            for (int col = 0; col < this.levelWidth; col++) {
                int pixel = image.getRGB(col, row);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 128 && green == 128 && blue == 128) { //Grey = Floor
                    column.add(TileType.FLOOR);
                } else if (red == 0 && green == 0 && blue == 0) { //Black = Wall
                    column.add(TileType.WALL);
                } else if (red == 0 && green == 0 && blue == 255) { //Blue = Protagonist
                    column.add(TileType.PROTAGONIST);
                } else if (red == 0 && green == 255 && blue == 0) { //Green = Door //Add different blue if we want to have non right doors
                    column.add(TileType.DOOR_RIGHT);
                    this.doorMap.put(TileType.DOOR_RIGHT, new Point2D(col, row));
                } else if (red == 255 && green == 165 && blue == 0) { //Orange = Campfire / random chance of some other background but not solid.
                    column.add(TileType.CAMP_FIRE);
                } else if (red == 128 && green == 0 && blue == 128) { //Purple = Item
                    column.add(TileType.ITEM);
                } else if (red == 255 && green == 0 && blue == 1) { // Red = Enemy, (Blue = 1) = Grunt
                    column.add(TileType.GRUNT);
                } else if (red == 255 && green == 0 && blue == 2) { // Red = Enemy, (Blue = 2) = Bomber
                    column.add(TileType.BOMBER);
                } else if (red == 255 && green == 0 && blue == 3) { // Red = Enemy, (Blue = 3) = Archer
                    column.add(TileType.ARCHER);
                } else {
                    column.add(TileType.NULLTILE);
                }
            }
            this.tiles.add(column);
        }
    }

    public void loadLevel(Game game) {
        Handler.clearAllObjects();

        for (int row = 0; row < this.levelHeight; row++) {
            for (int col = 0; col < this.levelWidth; col++) {
                TileType tile = this.tiles.get(row).get(col);
                //May be able to move sprite width into respective class's later. Keep here for testing
                switch (tile) {
                    case CAMP_FIRE:
                        Handler.addWall(col + row * this.levelWidth, new AnimationWall(col, row, PreLoadedImages.campFireSpriteSheet, CAMP_FIRE_SPRITE_WIDTH,
                                CAMP_FIRE_SPRITE_HEIGHT, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE, 4,0,0));
                        break;

                    case WALL:
                        Handler.addWall(col + row * this.levelWidth, new Wall(col,row, PreLoadedImages.tileSpriteSheet, Tile_SPRITE_WIDTH,
                                Tile_SPRITE_HEIGHT,CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE, 0,2));
//                        continue; //Continue if wall is a solid sprite, otherwise consider break to draw tile underneath.
                        break;
                    case PROTAGONIST:
                        Protagonist tempProtagonist = new Protagonist(col, row, PreLoadedImages.protagonistSpriteSheet, PROTAGONIST_SPRITE_WIDTH,
                                PROTAGONIST_SPRITE_HEIGHT, (int) (PROTAGONIST_SPRITE_WIDTH * Game.SCALE * PROTAGONIST_SPRITE_SCALE),
                                (int) (PROTAGONIST_SPRITE_HEIGHT * Game.SCALE * PROTAGONIST_SPRITE_SCALE), this.levelWidth);
                        Handler.addPlayer(tempProtagonist);
                        game.setProtagonist(tempProtagonist);
                        this.tiles.get(row).set(col, TileType.FLOOR);
                        break;

                    case DOOR_UP:
                        Point2D upDoorLocation = this.doorMap.get(TileType.DOOR_UP);
                        Handler.addDoor(new Door((int)upDoorLocation.getX(), (int)upDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber - this.mapWidth,TileType.DOOR_UP));
                        break;

                    case DOOR_RIGHT:
                        Point2D rightDoorLocation = this.doorMap.get(TileType.DOOR_RIGHT);
                        Handler.addDoor(new Door((int)rightDoorLocation.getX(), (int)rightDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber + 1,TileType.DOOR_RIGHT));
                        break;
//
                    case DOOR_DOWN:
                        Point2D downDoorLocation = this.doorMap.get(TileType.DOOR_DOWN);
                        Handler.addDoor(new Door((int)downDoorLocation.getX(), (int)downDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber + this.mapWidth,TileType.DOOR_DOWN));
                        break;

                    case DOOR_LEFT:
                        Point2D leftDoorLocation = this.doorMap.get(TileType.DOOR_LEFT);
                        Handler.addDoor(new Door((int)leftDoorLocation.getX(), (int)leftDoorLocation.getY(), PreLoadedImages.doorSpriteSheet,
                                DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_RENDER_WIDTH, DOOR_RENDER_HEIGHT, this.levelNumber - 1,TileType.DOOR_LEFT));
                        break;

                    case FLOOR: //This is just here so if we add tiles with different textures, we can differentiate and create floor with different spreadsheet row/col
                        break;

                    case ITEM:
                        Handler.addWall(col + row * this.levelWidth, new NullTile(col, row, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, false));
                        continue;

                    case GRUNT:
                        Handler.addEnemy(new Grunt(col,row,PreLoadedImages.gruntSpriteSheet, GRUNT_SPRITE_WIDTH, GRUNT_SPRITE_HEIGHT, GRUNT_SPRITE_WIDTH * Game.SCALE,
                                GRUNT_SPRITE_HEIGHT * Game.SCALE, this.levelWidth, true));
                        break;

                    default:
                        Handler.addWall(col + row * this.levelWidth, new NullTile(col, row, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, true));
                        continue;
                }
                //Move this into its own switch section above and add a tile to handler in the constructor of the other tiles that require a tile eg protagonist, door, enemy
                Handler.addFloor(new Floor(col, row, PreLoadedImages.tileSpriteSheet, Tile_SPRITE_WIDTH, Tile_SPRITE_HEIGHT, Game.PIXEL_UPSCALE, Game.PIXEL_UPSCALE, 0, 0));
            }
        }
        Handler.updateEnemyTarget(game.getProtagonist()); //As enemies can be added before protagonist making their target null. So add at the end.
        Handler.udateCharacterLevelWidth(this.levelWidth);
    }

}
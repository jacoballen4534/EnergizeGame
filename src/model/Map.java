package model;

import sample.Game;

import java.awt.image.BufferedImage;
import java.security.Key;
import java.util.ArrayList;

enum TileType {
    FLOOR,
    WALL,
    CAMP_FIRE,
    PROTAGONIST,
    DOOR,
    ENEMY
}

public class Map {
    //Holds all the levels.
    private ArrayList<ArrayList<TileType>> levels = new ArrayList<>();
    private int currentLevelNumber = 0;
    private int currentLevelWidth;
    private int currentLevelHeight;
    //May need to put this into a level class so we dont need to get the floor each time.
    private ArrayList<Floor> currentLevelFloorTiles = new ArrayList<>();
    private ArrayList<TileType> currentLevel;
    Game game; //To add the protagonist to.

    //////////////Macros///////////
    private final int FLOOR_SPRITE_WIDTH = 32;
    private final int FLOOR_SPRITE_HEIGHT = 32;


    private final int PROTAGONIST_SPRITE_WIDTH = 400;
    private final int PROTAGONIST_SPRITE_HEIGHT = 296;

    private final int CAMP_FIRE_SPRITE_WIDTH = 64;
    private final int CAMP_FIRE_SPRITE_HEIGHT = 64;

    private final int DOOR_SPRITE_WIDTH = 72;
    private final int DOOR_SPRITE_HEIGHT = 96;



    public Map(Game game){
        this.game = game;
        loadTutorialRoomImage();
        this.currentLevel = levels.get(0);
        loadLevel(0);
    }

    public  ArrayList<TileType> getLevel(int levelNumber) {
        if (this.levels.size() >= levelNumber) {
            //By only updating currentLevelImage, this will ensure if a non existent level is requested it will still return a valid level
            this.currentLevel = this.levels.get(levelNumber);
            this.currentLevelNumber = levelNumber;
        }
        return this.currentLevel;
    }

    private void loadTutorialRoomImage() {
        this.currentLevelWidth = PreLoadedImages.tutorialRoom.getWidth();
        this.currentLevelHeight = PreLoadedImages.tutorialRoom.getHeight();
        ArrayList<TileType> level = new ArrayList<>();
        for (int y = 0; y < PreLoadedImages.tutorialRoom.getHeight(); y++) {
            for (int x = 0; x < PreLoadedImages.tutorialRoom.getWidth(); x++) {
                int pixel = PreLoadedImages.tutorialRoom.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 0 && green == 0 && blue == 0) { //Black = Wall
                    level.add(TileType.WALL);
                } else if (red == 0 && green == 0 && blue == 255) { //Blue = Protagonist
                    level.add(TileType.PROTAGONIST);
                } else if (red == 214 && green == 127 && blue == 255) { //Pink = Door
                    level.add(TileType.DOOR);
                } else if (red == 255 && green == 106 && blue == 0) { //Pink = Door
                    level.add(TileType.CAMP_FIRE);
                } else {
                    level.add(TileType.FLOOR);
                }
            }
            levels.add(level);
        }
    }


    public int getCurrentLevelNumber() {
        return this.currentLevelNumber;
    }

    public int getCurrentLevelHeight() {
        return this.currentLevelHeight;
    }

    public int getCurrentLevelWidth() {
        return this.currentLevelWidth;
    }

    public void loadLevel(int levelNumber) {
        Handler.clearObjectList();
        this.currentLevel = levels.get(levelNumber);
        for (int y = 0; y < this.currentLevelHeight; y++) {
            for (int x = 0; x < this.currentLevelWidth; x++) {

                TileType tile = this.currentLevel.get(x + y * this.currentLevelWidth);

                //Can move sprite width into respective class's later. Keep here for testing
                switch (tile) {
                    case CAMP_FIRE:
                        Handler.addObject(new Wall(x,y, PreLoadedImages.campFireSpriteSheet, CAMP_FIRE_SPRITE_WIDTH,
                                CAMP_FIRE_SPRITE_HEIGHT,CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE));
                        Handler.addTile(new Floor(x,y,PreLoadedImages.floorSpriteSheet, FLOOR_SPRITE_WIDTH,FLOOR_SPRITE_HEIGHT,FLOOR_SPRITE_WIDTH * Game.SCALE,FLOOR_SPRITE_HEIGHT * Game.SCALE,13,0));
                        break;

                    case WALL:
//                        Handler.addObject(new Wall(x,y, PreLoadedImages.campFireSpriteSheet, CAMP_FIRE_SPRITE_WIDTH,
//                                CAMP_FIRE_SPRITE_HEIGHT,CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE));
                        Handler.addObject(new NullTile(x,y,CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, true));
                        Handler.addTile(new Floor(x,y,PreLoadedImages.floorSpriteSheet, FLOOR_SPRITE_WIDTH,FLOOR_SPRITE_HEIGHT,FLOOR_SPRITE_WIDTH * Game.SCALE,FLOOR_SPRITE_HEIGHT * Game.SCALE,13,0));
                        break;

                    case PROTAGONIST:
                        Protagonist tempProtagonist = new Protagonist(x,y, PreLoadedImages.protagonistSpriteSheet, PROTAGONIST_SPRITE_WIDTH,
                                PROTAGONIST_SPRITE_HEIGHT, (int)(PROTAGONIST_SPRITE_WIDTH * Game.SCALE * 0.35), (int)(PROTAGONIST_SPRITE_HEIGHT * Game.SCALE * 0.35), this.game.getKeyInput());
                        Handler.addObject(tempProtagonist);
                        game.setProtagonist(tempProtagonist);
                        Handler.addTile(new Floor(x,y,PreLoadedImages.floorSpriteSheet, FLOOR_SPRITE_WIDTH,FLOOR_SPRITE_HEIGHT,FLOOR_SPRITE_WIDTH * Game.SCALE,FLOOR_SPRITE_HEIGHT * Game.SCALE,13,0));
                        break;

                    case DOOR:
                        Handler.addObject(new Door(x,y,PreLoadedImages.doorSpriteSheet, DOOR_SPRITE_WIDTH,DOOR_SPRITE_HEIGHT,DOOR_SPRITE_WIDTH * Game.SCALE,DOOR_SPRITE_HEIGHT * Game.SCALE, this.currentLevelNumber,1));
                        break;

                    case FLOOR:
                        //Can add different tiles texture, just pass in a different spriteSheet row and col
                        Handler.addTile(new Floor(x,y,PreLoadedImages.floorSpriteSheet, FLOOR_SPRITE_WIDTH,FLOOR_SPRITE_HEIGHT,FLOOR_SPRITE_WIDTH * Game.SCALE,FLOOR_SPRITE_HEIGHT * Game.SCALE,13,0));
                        break;

                }
            }
        }
    }
}

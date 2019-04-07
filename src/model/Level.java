package model;

import sample.Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

enum TileType {
    FLOOR,
    WALL,
    CAMP_FIRE,
    PROTAGONIST,
    DOOR,
    ENEMY,
    NULLTILE
}

public class Level {
    private ArrayList<ArrayList<TileType>> tiles = new ArrayList<>();
    private int levelNumber;
    private int levelWidth;
    private int levelHeight;
    Game game; //To add the protagonist to.
    //Add everything else to the handler

    //////////////Macros///////////
    private final int FLOOR_SPRITE_WIDTH = 32;
    private final int FLOOR_SPRITE_HEIGHT = 32;

    private final int PROTAGONIST_SPRITE_WIDTH = 400;
    private final int PROTAGONIST_SPRITE_HEIGHT = 296;

    private final int CAMP_FIRE_SPRITE_WIDTH = 64;
    private final int CAMP_FIRE_SPRITE_HEIGHT = 64;

    private final int DOOR_SPRITE_WIDTH = 72;
    private final int DOOR_SPRITE_HEIGHT = 96;


    public Level(Game game, BufferedImage image, int levelNumber) { //Makes a level from an image

        this.game = game;
        ProcessImage(image);
        this.levelNumber = levelNumber;
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

    private void ProcessImage(BufferedImage image) {
        this.levelWidth = image.getWidth();
        this.levelHeight = image.getHeight();
        for (int row = 0; row < this.levelHeight; row++) {
            ArrayList<TileType> column = new ArrayList<>();
            for (int col = 0; col < this.levelWidth; col++) {
                int pixel = PreLoadedImages.tutorialRoom.getRGB(row, col);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 128 && green == 128 && blue == 128) { //Grey = Floor
                    column.add(TileType.FLOOR);
                } else if (red == 0 && green == 0 && blue == 0) { //Black = Wall
                    column.add(TileType.WALL);
                } else if (red == 0 && green == 0 && blue == 255) { //Blue = Protagonist
                    column.add(TileType.PROTAGONIST);
                } else if (red == 0 && green == 255 && blue == 0) { //Green = Door
                    column.add(TileType.DOOR);
                } else if (red == 255 && green == 165 && blue == 0) { //Orange = Campfire / item
                    column.add(TileType.CAMP_FIRE);
                } else {
                    column.add(TileType.NULLTILE);
                }
            }
            this.tiles.add(column);
        }
    }

    public void loadLevel() {
        Handler.clearAllObjects();

        for (int row = 0; row < this.levelHeight; row++) {
            for (int col = 0; col < this.levelWidth; col++) {
                TileType tile = this.tiles.get(col).get(row);
                //May be able to move sprite width into respective class's later. Keep here for testing
                switch (tile) {
                    case CAMP_FIRE:
                        Handler.addObject(new Wall(col, row, PreLoadedImages.campFireSpriteSheet, CAMP_FIRE_SPRITE_WIDTH,
                                CAMP_FIRE_SPRITE_HEIGHT, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE));
                        break;

                    case WALL:
//                        Handler.addObject(new Wall(x,y, PreLoadedImages.campFireSpriteSheet, CAMP_FIRE_SPRITE_WIDTH,
//                                CAMP_FIRE_SPRITE_HEIGHT,CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_HEIGHT * Game.SCALE));
                        Handler.addObject(new NullTile(col, row, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, true));
                        continue;

                    case PROTAGONIST:
                        Protagonist tempProtagonist = new Protagonist(col, row, PreLoadedImages.protagonistSpriteSheet, PROTAGONIST_SPRITE_WIDTH,
                                PROTAGONIST_SPRITE_HEIGHT, (int) (PROTAGONIST_SPRITE_WIDTH * Game.SCALE * 0.35), (int) (PROTAGONIST_SPRITE_HEIGHT * Game.SCALE * 0.35), this.game.getKeyInput());
                        Handler.addObject(tempProtagonist);
                        game.setProtagonist(tempProtagonist);
                        break;

                    case DOOR:
                        Handler.addObject(new Door(col, row, PreLoadedImages.doorSpriteSheet, DOOR_SPRITE_WIDTH, DOOR_SPRITE_HEIGHT, DOOR_SPRITE_WIDTH * Game.SCALE, DOOR_SPRITE_HEIGHT * Game.SCALE, this.levelNumber, 1));
                        break;

                    case FLOOR:
                        //Can add different tiles texture, just pass in a different spriteSheet row and col
                        break;

                    default:
                        Handler.addObject(new NullTile(col, row, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, CAMP_FIRE_SPRITE_WIDTH * Game.SCALE, true));
                        continue;
                }
                //Move this into its own switch section above and add a tile to handler in the constructor of the other tiles that require a tile eg protagonist, door, enemy
                Handler.addTile(new Floor(col, row, PreLoadedImages.floorSpriteSheet, FLOOR_SPRITE_WIDTH, FLOOR_SPRITE_HEIGHT, Game.PIXEL_UPSCALE, Game.PIXEL_UPSCALE, 13, 0));
            }
        }
    }

    //Generate level
}
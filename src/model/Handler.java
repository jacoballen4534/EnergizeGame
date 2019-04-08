package model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import sample.Game;

import java.awt.*;
import java.util.*;

public class Handler { //This class will hold all the game objects and is responsible for rendering each one
    //////////////////OLD CODE//////////////////
    private static ArrayList<GameObject> gameObjects = new ArrayList<>();
    private static ArrayList<Tile> tiles = new ArrayList<>(); //Need tiles separate to render first
    ///////////////////////////////////////////////


    private static ArrayList<Floor> floors = new ArrayList<>(); //Holds hte floor tiles. these are rendered first and dont need to be check for colisions.
    private static HashMap<Integer, GameObject> walls = new HashMap<>();//Holds all the walls and null tiles, with their position in the form x + y*width
    private static ArrayList<Character> characters = new ArrayList<>(); //Hold all players and enemy's
    private static ArrayList<Door> doors = new ArrayList<>();//Holds the various doors in the level. Used to load next level.
    private static ArrayList<Item> pickups = new ArrayList<>(); //Holds the scrolls and keys that are left on the map. Chests?
    private static Map map;



    //Use a timeline instead of render, or tick methods to control the speed of the animation
    public static Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
        for (GameObject object: gameObjects) {
            object.updateSprite();
        }
    }));


    public static void setMap (model.Map _map) {
        map = _map;
    }
    public static void tick(double cameraX, double cameraY) {
        for (Character character : characters) {
            character.tick(cameraX, cameraY);
        }

        //////////////OLD CODE//////////////////////
        for (GameObject gameObject : gameObjects) {
            gameObject.tick(cameraX,cameraY);
        }
    }

    public static void render(GraphicsContext graphicsContext, double cameraX, double cameraY){
        //Render all floor tiles first, then objects
        for (Floor floor : floors) {
            floor.render(graphicsContext,cameraX,cameraY);
        }

        walls.forEach((location, wall) -> {
            wall.render(graphicsContext,cameraX,cameraY);
        });

        for (Character character : characters) {
            character.render(graphicsContext,cameraX,cameraY);
        }

        for (Door door : doors) {
            door.render(graphicsContext,cameraX,cameraY);
        }

        for (Item pickup : pickups) {
//            pickup.render //TODO: Implement pickup items.
        }


        ///////////OLD CODE//////////
        for (Tile tile : tiles) {
            tile.render(graphicsContext, cameraX,cameraY);
        }

        for (GameObject object : gameObjects) {
            object.render(graphicsContext,cameraX,cameraY);
        }
        ////////////////////////////////
    }

    ///////////////////OLD CODE////////////////
    public static void addObject(GameObject object) {
        gameObjects.add(object);
    }
    public static void addTile (Tile tile) {
        tiles.add(tile);
    }
    ///////////////////////////////////////


    public static void addWall (int location, GameObject wall) {
        walls.put(location, wall);
    }

    public static void addCharacter (Character character) {
        characters.add(character);
    }

    public static void addDoor (Door door) {
        doors.add(door);
    }

    public static void addFloor (Floor floor) {
        floors.add(floor);
    }

    public static void addPickup (Item pickup) {
        pickups.add(pickup);
    }




    public static void clearAllObjects() {
        /////////OLD CODE ////////////
        gameObjects.clear();
        tiles.clear();
        /////////////////////////////
        walls.clear();
        characters.clear();
        doors.clear();
        floors.clear();
        pickups.clear();
    }

    public static boolean checkCollision (Character character, double cameraX, double cameraY) {
//        //TODO:Implament items and inventory first
//        for (Item pickup : pickups) {
//            if (pickups.inCameraBounds(cameraX,cameraY) && character.getBounds().intersects(pickup.getBounds())) {
//                character.pickup(pickup); //Enemies will have a blank method here, while protagonist will be able to pick the item up
//            }
//        }

        for (Door door : doors) { //If a door is on screen and the character is going through it, load the next level
            if (door.inCameraBounds(cameraX,cameraY) && character.getBounds().intersects(door.getBounds())) {
//                map.loadLevel(door.getNextLevel()); //TODO: Add more levels to load.
            }
        }

        for (Character otherCharacter : characters) {
            if (!character.equals(otherCharacter) && otherCharacter.inCameraBounds(cameraX,cameraY)
                    && character.getBounds().intersects(otherCharacter.getBounds())) {
                return true;
            }
        }

        //Only check walls that are on screen
        //TODO: Only check walls in a 2 square radius. Using
        // character x = (Character.x/PIXEL_UPSCALE) + 1, character y = (Character.y/PIXEL_UPSCALE) + 1
        //Instead of Math.celi just let the integer truncate and then add 1. Using 2 square radius to allow for minor offsets

        for (java.util.Map.Entry<Integer, GameObject> entry : walls.entrySet()) {
            GameObject wall = entry.getValue();
            if (wall.inCameraBounds(cameraX,cameraY) && character.getBounds().intersects(wall.getBounds())) {
                return true;
            }
        }

//        return false;


        ////////OLD CODE//////////////////////////
        for (GameObject object : gameObjects) {
            if (!object.equals(character) && character.getBounds().intersects(object.getBounds())) {
                return true;
            }
        }
        return false;
        /////////////////////////////////////////
    }
}

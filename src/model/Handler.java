package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Game;

import java.util.*;

public class Handler { //This class will hold all the game objects and is responsible for rendering each one

    private static ArrayList<Floor> floors = new ArrayList<>(); //Holds the floor tiles. these are rendered first and dont need to be check for colisions.
    private static TreeMap<Integer, GameObject> walls = new TreeMap<>();//Holds all the walls and null tiles, with their position in the form x + y*width
    //private static ArrayList<Character> characters = new ArrayList<>();
    private static ArrayList<Protagonist> otherPlayers = new ArrayList<>(); //Hold all players. May remove this as the handler needs to hold the protagonist on its own,
    // so the only other player will be in multilayer, where the handler can hold other player on its own also
    private static ArrayList<Enemy> enemies = new ArrayList<>(); //Hold all enemies
    private static ArrayList<Door> doors = new ArrayList<>();//Holds the various doors in the level. Used to load next level.
    private static ArrayList<Item> pickups = new ArrayList<>(); //Holds the scrolls and keys that are left on the map. Chests?
    private static Map map;
    private static Camera camera;
    private static boolean timelineIsPaused = false;
    private static HUD hud;
    private static Game game;
    private static Protagonist protagonist; //Tie the protagonist to this handler. Used for multiplayer when there are multiple protagonist's.
    //private static KeyInput keyInput = new KeyInput(getKeyInput());

    //Use a timeline instead of render, or tick methods to control the speed of the animation
    public static Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {

        for (Door door : doors) {
            if (door.inCameraBounds(camera.getX(), camera.getY())) {
                door.updateSprite();
            }
        }

        for (java.util.Map.Entry<Integer, GameObject> entry : walls.entrySet()) {
            GameObject wall = entry.getValue();
            if (wall.inCameraBounds(camera.getX(),camera.getY())) {
                wall.updateSprite();
            }
        }

        for (Protagonist player : otherPlayers) {
            if (player.getLevelNumber() == protagonist.getLevelNumber()) {
                player.updateSprite();
            }
        }

        protagonist.updateSprite();

        for (Enemy enemy : enemies) {
            enemy.updateSprite(); //Dont check camera so spells do damage to all.
        }
    }));

    public static void setCamera(Camera _camera) {
        camera = _camera;
    }

    public static void setMap (Map _map) {
        map = _map;
    }

    public static void clearForNewGame() {
        clearAllObjects();
        otherPlayers.clear();
    }

    public static void setGame (Game _game) {
        game = _game;
    }

    public static void tick(double cameraX, double cameraY, KeyInput keyInput, String onlineComands) {

        protagonist.tick(cameraX, cameraY, keyInput);

        for (Protagonist player : otherPlayers) {
            if(!player.equals(protagonist)) {
                player.tick(cameraX, cameraY, onlineComands);
            }
        }
        for (Enemy enemy : enemies) {
            enemy.tick(cameraX, cameraY, map.getCurrentLevel());
        }

        if (enemies.isEmpty()) {
            for (Door door : doors) {
                door.openDoor();
            }
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

        for (Door door : doors) {
            door.render(graphicsContext,cameraX,cameraY);
        }

        for (Item pickup : pickups) {
            pickup.render(graphicsContext,cameraX,cameraY);
        }

        for (Enemy enemy : enemies){
            enemy.render(graphicsContext,cameraX,cameraY);
        }

        for (Protagonist player: otherPlayers) {
            if (player.getLevelNumber() == protagonist.getLevelNumber()) {
                player.render(graphicsContext,cameraX,cameraY);
            }
        }

        protagonist.render(graphicsContext, cameraX, cameraY);

//        hud.render(graphicsContext,cameraX,cameraY);//Need to render hud last, as it is the top overlay.
    }

    public static void updateEnemyTarget () {
        for (Enemy enemy : enemies) {
//            ------change up the targets
            enemy.updateTarget(protagonist);
        }
    }

    public static void addWall (int location, GameObject wall) {
        walls.put(location, wall);
    }

    public static void addPlayer (OnlinePlayer player) {
        Platform.runLater(() -> otherPlayers.add(player));
    }

    public static void removePlayer (Protagonist player) {
        Platform.runLater(() -> { //Use runLater to safely remove characters
            otherPlayers.remove(player);
        });
    }

    public static void removePlayer (int id) {
        Platform.runLater(() -> { //Use runLater to safely remove characters
            for (int i = 0; i < otherPlayers.size(); i++) {
                if (otherPlayers.get(i).id == id) {
                    otherPlayers.remove(otherPlayers.get(i));
                    break;
                }
            }
        });
    }

    public static void addEnemy (Enemy enemy, Pair<Integer, Integer> spawnID) {
        enemies.add(enemy);
        enemy.setSpawnID(spawnID);
    }

    public static void removeEnemy (Enemy enemy) { //Tell all clients to remove this from their map
        Platform.runLater(() -> {
            map.removeObject(enemy);
            enemies.remove(enemy);
        });
    }

    public static void addDoor (Door door) {
        doors.add(door);
    }

    public static void addFloor (Floor floor) {
        floors.add(floor);
    }

    public static void addPickup (Item pickup, Pair<Integer, Integer> spawnID) {
        pickups.add(pickup);
        pickup.setSpawnID(spawnID);
    }

    public static void removePickup (Item pickup) {
        Platform.runLater(() -> {
            pickups.remove(pickup);
            map.removeObject(pickup);
        });
    }

    public static void removeFromMap (int level, int location) { //Sent from server. Remove from map, and if it is on the current level, remove it from the handler also.
        Platform.runLater(() -> {
            map.removeObject(level, location);
            if (level == map.getCurrentLevelNumber()) { //Remove if from the handler aswell
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    if (enemy.getSpawnID().getKey() == level && enemy.getSpawnID().getValue() == location) {
                        enemies.remove(enemy);
                    }
                }
                for (int i = 0; i < pickups.size(); i++) {
                    Item item = pickups.get(i);
                    if (item.getSpawnID().getKey() == level && item.getSpawnID().getValue() == location) {
                        pickups.remove(item);
                    }
                }
            }
        });
    }

    public static void loadBossRoom() {
        map.loadLevel(8055);//Load boss level
    }

    public static void disconnectFromServer() {
        protagonist.disconnect();
    }

    public static void pauseUnpauseTimeline(){
        if (timelineIsPaused){
            timeline.play();
        }
        else{
            timeline.pause();
        }
        timelineIsPaused = !timelineIsPaused;
    }

    public static void pauseTimeline(){timeline.pause();}

    public static void unpauseTimeline(){timeline.play();}

    public static void clearAllObjects() { //Not safe to use while in the middle of a level
        walls.clear();
        enemies.clear();
        doors.clear();
        floors.clear();
        pickups.clear();
        //Dont clear the protagonist as this gets set once and moved through the rooms
    }

    public static void attack(Protagonist protagonist) {
        for (Enemy enemy: enemies){
            if (enemy.inCameraBounds(camera.getX(), camera.getY()) && protagonist.getAttackBounds().intersects(enemy.getBounds())){
                enemy.getHit(protagonist.getAttackDamage()); //Pass in damage which varies based on weapon type
            }
        }
    }

    public static void attack(Enemy enemy) {
        for (Protagonist player: otherPlayers){
            if (enemy.getAttackBounds().intersects(player.getBounds())){
                player.getHit(enemy.getAttackDamage());  //Pass in damage which varies based on enemy type
            }
        }

        if (enemy.getAttackBounds().intersects(protagonist.getBounds())){
            protagonist.getHit(enemy.getAttackDamage());  //Pass in damage which varies based on enemy type
        }

    }

    public static void fireScrollAttack(Scroll scroll) {
        for (Enemy enemy : enemies) {
            enemy.getHit(scroll.getDamage());
        }
    }

    public static void freezeEnemys(Scroll scroll) {
        for (Enemy enemy : enemies) {
            enemy.freeze(scroll.getFreezeDuration());
        }
    }

    public static void blowEnemysAway(Scroll scroll) {
        for (Enemy enemy : enemies) {
            enemy.blowAway(scroll.getWindDuration());
        }
    }

    public static void setProtagonist (Protagonist _protagonist) {
        protagonist = _protagonist;
    }


    public static void updateCharacterLevelWidth(int newLevelWidth) {
        for (Enemy enemy : enemies) {
            enemy.updateLevelWidth(newLevelWidth);
        }

        for (Protagonist player : otherPlayers) {
            player.updateLevelWidth(newLevelWidth);
        }

        protagonist.updateLevelWidth(newLevelWidth);
    }

    public static void updateCharacterLevelNumber(int newLevelNumber) {
        for (Protagonist player : otherPlayers) {
            player.updateLevelNumber(newLevelNumber);
        }
        protagonist.updateLevelNumber(newLevelNumber);
    }

    public static boolean checkCollision (Character character) {
        for (Item pickup : pickups) {
            if (character.getBounds().intersects(pickup.getBounds())) {
                if (character.isProtagonist() && !character.getInventory().isFull() && !character.getInventory().containsItem(pickup) &&
                        (character.getInventory().getEquippedItem() == null || !character.getInventory().getEquippedItem().equals(pickup))){
                    System.out.println("can pick up " + pickup.getName());
                    character.pickup(pickup);
                    removePickup(pickup);
                }
            }
        }

        for (Door door : doors) { //If a door is on screen and the character is going through it, load the next level
            if (character.getBounds().intersects(door.getBounds())) { //Might need to check out of camera bounds for enemies running into doors
                if (door.isOpen() && character.equals(protagonist)) {
                    //Need to make this thread safe as we are changing things on the main thread. So use runLater
                    Platform.runLater(() -> {

                        map.loadLevel(door.getNextLevel());
                        double nextLevelX, nextLevelY; //This is the non up-scaled value

                        //Get opposite door type from the intersection door, eg if intersects with Door_Right, get Door_Left from next level and set protagonist there.
                        TreeMap<TileType, Point2D> currentLevelDoors = map.getCurrentLevel().getDoors();

                        switch (door.getDoorType()) { //Check the type of the door we hit. Then move player to the corresponding door,
                            // plus an offset so they dont spawn inside the door.
                            case DOOR_UP:
                                nextLevelX = currentLevelDoors.get(TileType.DOOR_DOWN).getX();
                                nextLevelY = currentLevelDoors.get(TileType.DOOR_DOWN).getY() - 1.3; //Protagonist height + door border
                                break;

                            case DOOR_RIGHT:
                                nextLevelX = currentLevelDoors.get(TileType.DOOR_LEFT).getX() + 1; //Door is 1 tile wide
                                nextLevelY = currentLevelDoors.get(TileType.DOOR_LEFT).getY();
                                break;

                            case DOOR_DOWN:
                                nextLevelX = currentLevelDoors.get(TileType.DOOR_UP).getX();
                                nextLevelY = currentLevelDoors.get(TileType.DOOR_UP).getY() + 1.65; //Door height + protagonist border
                                break;

//                        case DOOR_LEFT:
                            default: //So it doesnt complain about x,y not being initialized
                                nextLevelX = currentLevelDoors.get(TileType.DOOR_RIGHT).getX() - 1; //Protagonist is 1 tile wide
                                nextLevelY = currentLevelDoors.get(TileType.DOOR_RIGHT).getY();
                                break;
                        }

                        protagonist.setX(nextLevelX * Game.PIXEL_UPSCALE);
                        protagonist.setY(nextLevelY * Game.PIXEL_UPSCALE);

                    });
                    return false;
                } else {
                    return true;
                }

            }
        }

        if (!character.isProtagonist()) {
            for (Protagonist player : otherPlayers) {
                if (character.getBounds().intersects(player.getBounds())) {
                    return true;
                }
            }
            if (character.getBounds().intersects(protagonist.getBounds())) {
                return true;
            }
        }

        for (Enemy enemy: enemies){
            if (!character.equals(enemy) && character.getBounds().intersects(enemy.getBounds())){
                return true;
            }
        }

        //Only check walls that are on screen
        // Only check walls in a 2 square radius.
        // Using character x = (Character.x/PIXEL_UPSCALE) + 1, character y = (Character.y/PIXEL_UPSCALE) + 1
        //Instead of Math.ceil just let the integer truncate and then add 1. Using 2 square radius to allow for minor offsets

        int characterX = (int)(character.getX() / Game.PIXEL_UPSCALE) + 1;
        int characterY = (int)(character.getY() / Game.PIXEL_UPSCALE) + 1;
        int coord;

        for (int x = characterX - 2; x <= characterX + 2; x++) { //Get tiles in 2 square radius
            for (int y = characterY - 2; y <= characterY + 2; y++) {
                coord =  x + y * character.getLevelWidth();
                if (walls.containsKey(coord)) {//If it exists, check if its intersecting.
                    GameObject wall = walls.get(coord);
                    if (character.getBounds().intersects(wall.getBounds())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

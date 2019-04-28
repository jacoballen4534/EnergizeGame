package model;

import FXMLControllers.EndScreenController;
import FXMLControllers.MainMenuController;
import Multiplayer.Server;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Game;
import sample.SoundController;

import java.io.IOException;
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
            enemy.updateSprite(); //Don't check camera so spells do damage to all.
        }
    }));

    public static void gameOver(boolean victory) {
    try {
        game.pause();
        int time = protagonist.getMinutes() * 60 + protagonist.getSeconds();

        EndScreenController.SetScore(victory, protagonist.getItemsCollected(), protagonist.getEnemysKilled(), time);
        Stage stage = (Stage) Game.getRoot().getScene().getWindow();
        stage.setTitle("Game Over");
        Game.getRoot().getChildren().setAll((AnchorPane) new FXMLLoader().load(MainMenuController.class.getResourceAsStream("/fxmls/endScreen.fxml")));
        SoundController.playMusic("titleBGM");
    } catch (IOException e) {
        e.printStackTrace();
    }

    }

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

    public static void finishGame(){
        //Change to end screen
    }

    public static void tick(double cameraX, double cameraY, KeyInput keyInput, String onlineCommands) {

        protagonist.tick(cameraX, cameraY, keyInput);

        for (Protagonist player : otherPlayers) {
            if(!player.equals(protagonist)) {
                player.tick(cameraX, cameraY, onlineCommands);
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
    }

    public static void updateEnemyTarget () {
        ShortestPath shortestPath = null;
        Protagonist closestPlayer = protagonist;

        if (map != null) { //If the map hasn't been setup yet. Just set all enemy targets as the host
            shortestPath = map.getCurrentLevel().getShortestPath();
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (shortestPath == null) {
                closestPlayer = protagonist;
                enemy.updateTarget(closestPlayer);
                continue;
            }
            int currentNodeId = (int)(enemy.x / Game.PIXEL_UPSCALE) + (int)(enemy.y / Game.PIXEL_UPSCALE) * map.getCurrentLevelWidth();
            int targetNodeId = (int)(protagonist.getX() / Game.PIXEL_UPSCALE) + (int)(protagonist.getY() / Game.PIXEL_UPSCALE) * map.getCurrentLevelWidth();
            double closestDist = Double.POSITIVE_INFINITY;
            if (protagonist.isAlive) {
                closestDist = shortestPath.shortestPathLength(currentNodeId, targetNodeId);
            }

            for (int j = 0; j < otherPlayers.size(); j++) {
                if (otherPlayers.get(j).getLevelNumber() == map.getCurrentLevelNumber()) {
                    targetNodeId = (int) (otherPlayers.get(j).getX() / Game.PIXEL_UPSCALE) + (int) (otherPlayers.get(j).getY() / Game.PIXEL_UPSCALE) * map.getCurrentLevelWidth();
                    int thisDist = shortestPath.shortestPathLength(currentNodeId, targetNodeId);
                    if (thisDist < closestDist) {
                        closestDist = thisDist;
                        closestPlayer = otherPlayers.get(j);
                    }
                }
            }
            enemy.updateTarget(closestPlayer);
            String commands = Server.PACKET_ENEMY_TARGET_UPDATE + Server.PACKET_ID + enemy.getSpawnID().getValue() + Server.PACKET_LEVEL_NUMBER
                    + enemy.getSpawnID().getKey() + Server.PACKET_ENEMY_TARGET + enemy.target.id + Server.PACKET_END;
            protagonist.sendToServer(commands);
        }
    }

    public static void updateEnemyTarget (String target) { //This is only called from the server to sync the enemy targets
        String[] temp;//For splitting
        target = target.split(Server.PACKET_ENEMY_TARGET_UPDATE)[1]; //Remove /etu/ Left with /id/364/ln/20/et/1/e/
        temp = target.split(Server.PACKET_ID + "|" + Server.PACKET_LEVEL_NUMBER); //Get the enemy id this packet is for
        int id = Integer.parseInt(temp[1]);
        target = temp[2]; // left with 20/et/1/e/
        temp = target.split(Server.PACKET_ENEMY_TARGET); // Gives [20] [1/e/]
        int levelNumber = Integer.parseInt(temp[0]);
        target = temp[1].split(Server.PACKET_END)[0];// Splits to [1]
        int targetId = Integer.parseInt(target);

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getSpawnID().getKey() == levelNumber && enemy.getSpawnID().getValue() == id) { //This is the enemy we want to update
                for (int j = 0; j < otherPlayers.size(); j++) {
                    if (otherPlayers.get(j).id == targetId) {
                        enemy.updateTarget(otherPlayers.get(j));
                        return;
                    }
                }
                enemy.updateTarget(protagonist); //If the target couldn't be found, or is the protag, then set it so that the enemy has a target.
                return;
            }
        }
    }

    public static void updateEnemyLocations (String target) { //This is only called from the server to sync the enemy location
        String[] temp;//For splitting
        target = target.split(Server.PACKET_ENEMY_UPDATE)[1]; //Remove /eu/ Left with /id/336/ln/60/pos/381.0,671.0/pos/1/e/
        temp = target.split(Server.PACKET_ID + "|" + Server.PACKET_LEVEL_NUMBER); //Get the enemy id this packet is for. Splits to [] [336] [60/pos/381.0,671.0/pos/1/e/]
        int id = Integer.parseInt(temp[1]);
        target = temp[2]; // left with 60/pos/381.0,671.0/pos/1/e/


        temp = target.split(Server.PACKET_POSITION); // Gives [60] [381.0,671.0] [1/e/]
        int levelNumber = Integer.parseInt(temp[0]);
        final double newX = Double.parseDouble(temp[1].split(",")[0]);//[381.0]
        final double newY = Double.parseDouble(temp[1].split(",")[1]);//[671.0]

        target = temp[2].split(Server.PACKET_END)[0];// Splits to [1]
        int targetId = Integer.parseInt(target);

//        System.out.print("Enemy on lvl " + levelNumber + " with ID: " + id + " is at (" + newX + "," + newY + ") Looking for client " + targetId);

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getSpawnID().getKey() == levelNumber && enemy.getSpawnID().getValue() == id) { //This is the enemy we want to update
                Platform.runLater(() -> enemy.serverUpdatePosition(newX, newY, camera.getX(), camera.getY()));
                break;
            }
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

    public static void loadBossRoom(double spawnX, double spawnY) {
        Platform.runLater(() -> {
           for (int i = 0; i < otherPlayers.size(); i++) {
               otherPlayers.get(i).x = spawnX;
               otherPlayers.get(i).y = spawnY;
           }
           protagonist.x = spawnX;
           protagonist.y = spawnY;
           map.loadLevel(8055);//Load boss level
           SoundController.changeMusic("bossBGM");
        });
    }

    public static void disconnectFromServer() {
        protagonist.disconnect();
    }

    public static void clearAllObjects() { //Not safe to use while in the middle of a level
        walls.clear();
        enemies.clear();
        doors.clear();
        floors.clear();
        pickups.clear();
        //Dont clear the protagonist as this gets set once and moved through the rooms
    }

    public static boolean attack(Protagonist protagonist) {
        for (Enemy enemy: enemies){
            if (enemy.inCameraBounds(camera.getX(), camera.getY()) && protagonist.getAttackBounds().intersects(enemy.getBounds())){
                enemy.getHit(protagonist.getAttackDamage()); //Pass in damage which varies based on weapon type
                return true; //indicates successful attack
            }
        }
        return false;
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
            if (enemy.inCameraBounds(camera.getX(), camera.getY())) {
                enemy.getHit(scroll.getDamage());
                enemy.setOnFire();
            }
        }
    }

    public static void freezeEnemies(Scroll scroll) {
        for (Enemy enemy : enemies) {
            if (enemy.inCameraBounds(camera.getX(), camera.getY())) {
                enemy.freeze(scroll.getFreezeDuration());
            }
        }
    }

    public static void blowEnemiesAway(Scroll scroll) {
        for (Enemy enemy : enemies) {
            if (enemy.inCameraBounds(camera.getX(), camera.getY())) {
                enemy.blowAway(scroll.getWindDuration());
            }
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
        protagonist.updateLevelNumber(newLevelNumber);
    }

    public static boolean checkCollision (Character character) {
        for (Item pickup : pickups) {
            if (character.getBounds().intersects(pickup.getBounds())) {
                /*
                * Conditions are:
                * - Is the character the protagonist?
                * - Is the protagonist's inventory full? //Not relevant with stackable items?
                * - Is the item already in the inventory? //This needs to change to allow stackable items
                * - Is the item already equipped?
                * */
                if (character.equals(protagonist) && !pickup.isInInventory()
                        && (protagonist.getInventory().getEquippedItem() == null || !protagonist.getInventory().getEquippedItem().equals(pickup))){
                    System.out.println("can pick up " + pickup.getName());
                    pickup.setInInventory(true);
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
                        Handler.updateEnemyTarget();

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

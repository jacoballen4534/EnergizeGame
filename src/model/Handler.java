package model;

import javafx.scene.canvas.GraphicsContext;
import sample.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Handler { //This class will hold all the game objects and is responsible for rendering each one
    //Using Treemap to store the height for render order.
    private static TreeMap<Integer, ArrayList<GameObject>> gameObjects = new TreeMap<>();

    public static void tick() {
        //Gets each height and ticks all object of that height
        gameObjects.forEach((index, objects) -> {
            for (GameObject object : objects) {
                object.tick();
            }
        });

    }

    public static void render(GraphicsContext graphicsContext){
        //gameObjects are sorted by height, so floor will be rendered first ...
        gameObjects.forEach((index, objects) -> {
            for (GameObject object : objects) {
                object.render(graphicsContext);
            }
        });
    }

    public static void addObject(GameObject object) {
        if (!gameObjects.containsKey(object.getRenderHeight())) {
            gameObjects.put(object.getRenderHeight(), new ArrayList<>());
        }
        gameObjects.get(object.getRenderHeight()).add(object);

    }

    public static void removeObject(GameObject object) {
        if (gameObjects.containsKey(object.getRenderHeight())) {
           gameObjects.get(object.getRenderHeight()).remove(object);
        }
    }

    public static boolean checkCollision (GameObject character) {
        for (Map.Entry<Integer, ArrayList<GameObject>> entry : gameObjects.entrySet()) {
            for (GameObject object : entry.getValue()) {
                if (!object.equals(character) && character.getBounds().intersects(object.getBounds())) {
                    return true;
                }
            }
        }
        return false;
    }

}

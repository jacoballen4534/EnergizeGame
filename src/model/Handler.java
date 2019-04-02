package model;

import javafx.scene.canvas.GraphicsContext;
import sample.Game;

import java.awt.*;
import java.util.ArrayList;

public class Handler { //This class will hold all the game objects and is responsible for rendering each one
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public void tick() {
        for (GameObject object : gameObjects) {
            object.tick();
        }
    }

    public void render(GraphicsContext graphicsContext){
        for (GameObject object : gameObjects) {
            object.render(graphicsContext);
        }
    }

    public void addObject(GameObject object) {
        gameObjects.add(object);
    }

    public void removeObject(GameObject object) {
        gameObjects.remove(object);
    }



}

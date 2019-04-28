package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

public class Weapon extends Item {
    public Weapon(String name, String description, int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        super(name, description, xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight);
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
    }

    @Override
    public void useItem(Protagonist user) {

    }
}

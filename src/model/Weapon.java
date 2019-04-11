package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

public class Weapon extends Item {
    public Weapon(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight) {
        super(xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight, renderWidth, renderHeight);
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
        //TODO
    }
}

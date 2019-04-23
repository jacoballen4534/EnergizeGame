package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

public class Pickup extends Item {
    public Pickup(String name, String description,int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        super(name, description, xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight);

    }

    @Override
    public void useItem() {

    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {

        if (this.inCameraBounds(cameraX,cameraY)) {
            graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
//            this.renderBoundingBox(graphicsContext);
        }

    }
}

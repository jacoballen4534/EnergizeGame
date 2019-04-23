package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

public class Scroll extends Item{

    public Scroll(String name, String description, int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        super(name, description,xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight);
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {

        if (this.inCameraBounds(cameraX,cameraY)) {
            graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
//            this.renderBoundingBox(graphicsContext);
        }

    }

    @Override
    public void useItem() {

    }
}

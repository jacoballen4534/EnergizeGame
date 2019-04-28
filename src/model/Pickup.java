package model;

import javafx.scene.canvas.GraphicsContext;
import sample.DifficultyController;

import java.awt.image.BufferedImage;

public class Pickup extends Item {
    public Pickup(String name, String description,int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        super(name, description, xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight);
    }

    @Override
    public void useItem(Protagonist user) {
        switch (this.name) {
            case "Health Kit":
                user.increaseHealth(DifficultyController.PICKUP_REFILL_AMOUNT.value);//Health kit restores 10 hp
                break;
            case "Energy Kit":
                user.increaseEnergy(DifficultyController.PICKUP_REFILL_AMOUNT.value);
                break;
        }
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {

        if (this.inCameraBounds(cameraX,cameraY)) {
            graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
//            this.renderBoundingBox(graphicsContext);
        }

    }
}

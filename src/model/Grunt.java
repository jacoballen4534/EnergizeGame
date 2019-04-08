package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grunt extends Enemy {

    public Grunt(int x, int y, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight, Character target) {
        super(x, y, image, spriteSheetWidth, spriteSheetHeight, renderWidth, renderHeight, target);

    }

    @Override
    void isPlayerInSight() {

    }

    @Override
    void findPlayer() {

    }

    @Override
    void attack() {

    }

    @Override
    void playSound() {

    }

    @Override
    void getHit() {

    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
        if (this.inCameraBounds(cameraX,cameraY)) {
            //Render
        }
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

}

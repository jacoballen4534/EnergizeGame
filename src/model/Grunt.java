package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grunt extends Enemy {

    public Grunt(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, Character target) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, target);
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }


    @Override
    void updateAnimationState() {
        //Fill out after sorting out what the different states are going to be.
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

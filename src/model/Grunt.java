package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grunt extends Enemy {

    public Grunt(int x, int y, boolean scale, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight) {
        super(x, y, scale, image, spriteSheetWidth, spriteSheetHeight);

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
    public void render(GraphicsContext graphicsContext) {

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    protected void loadSpriteSheet(BufferedImage image) {
        this.spriteSheet = new SpriteSheet(image, this.spriteWidth, this.spriteHeight);
    }
}

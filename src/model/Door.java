package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door extends Tile {
    private int currentLevel;
    private int nextLevel;

    public Door(int x, int y, boolean scale, BufferedImage image, int currentLevel, int nextLevel) {
        super(x, y, scale, image);
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;
    }

    @Override
    public void tick() {

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
        this.spriteSheet = new SpriteSheet(image, this.width, this.height);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getNextLevel() {
        return nextLevel;
    }
}

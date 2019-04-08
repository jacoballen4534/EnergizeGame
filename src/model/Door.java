package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door extends GameObject {
    private int currentLevel;
    private int nextLevel;

    public Door(int x, int y, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight, int currentLevel, int nextLevel) {
        super(x, y, image, spriteSheetWidth, spriteSheetHeight, renderWidth, renderHeight);
        //To load the appropriate next room
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;
        //Set up animation sprites
        this.animationMaxRow = 3;

        this.leftBorder = 5;
        this.rightBorder = 0;
        this.topBorder = 15;
        this.bottomBorder = 0;

        //Get a sub image from the full sprite sheet, then convert this to an FXImage so it can be drawn
        //Initialise image for first animation
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0, 0), null);
    }

    protected void updateSprite() {
        // Get the next sprite in the animation then convert it so it can be drawn.
        if (this.animationRow < this.animationMaxRow) {
            this.animationRow++;
        } else {
            this.animationRow = 0;

        }
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.animationCol,this.animationRow), null);

    }


    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
        if (this.inCameraBounds(cameraX,cameraY)) {
            graphicsContext.drawImage(this.jfxImage, this.x - this.leftBorder, this.y - this.topBorder, this.spriteWidth, this.spriteHeight);
        }
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
    public int getNextLevel() {
        return nextLevel;
    }
}

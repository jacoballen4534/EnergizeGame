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
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;

        this.animationMaxRow = 7;
        this.animationMaxCol = 11;

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0, 0), null); //Initialise image for first animation
    }

    @Override
    public void tick() {

    }

    protected void updateSprite() {
        this.leftBorder = 3;
        this.rightBorder = 0;
        this.topBorder = 15;
        this.bottomBorder = 0;

//        super.updateSprite();
        if (this.animationRow < this.animationMaxRow) {
            this.animationRow++;
        } else {
            this.animationRow = 4;

            if (this.animationCol < this.animationMaxCol) {
                this.animationCol++;
            } else {
                animationCol = 0;
            }
        }
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.animationCol,this.animationRow), null);

    }


    @Override
    public void render(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
    }


    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    protected void loadSpriteSheet(BufferedImage image) {
        this.spriteSheet = new SpriteSheet(image, this.spriteWidth, this.spriteHeight);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getNextLevel() {
        return nextLevel;
    }
}

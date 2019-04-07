package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {


    protected int x,y;
    protected boolean isSolid = true;
    protected boolean movable = true;
    protected int spriteWidth, spriteHeight; //This is the full width/height including any borders
    protected SpriteSheet spriteSheet; //The full sheet, able to get subsections to draw
    //This is the difference between where the sprite actually starts and spriteWidth / Height. Used to improve collisions.
    protected int leftBorder = 0;
    protected int rightBorder = 0;
    protected int topBorder = 0;
    protected int bottomBorder = 0;

    //This is updated in updateSprite for animations to cycle through the appropriate sprites
    protected int animationRow = 0;
    protected int animationCol = 0;
    protected int animationMaxRow = 0;
    protected int animationMaxCol = 0;

    protected int spriteDirection = 1; //This is used to flip the image, -1 is face left, 1 is face right.
    protected Image jfxImage; //To draw onto the canvas


    public GameObject(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight) {
        //Multiply by Floor width/height to scale up to real map size
        this.x = xLocation * Game.PIXEL_UPSCALE;
        this.y = yLocation * Game.PIXEL_UPSCALE;
        this.spriteWidth = spriteSheetWidth;
        this.spriteHeight = spriteSheetHeight;
        this.loadSpriteSheet(spriteSheet);
        this.spriteWidth = renderWidth;
        this.spriteHeight = renderHeight;
    }


    public abstract void tick();
    public abstract void render(GraphicsContext graphicsContext);
    protected Rectangle getBounds() {
        return new Rectangle(this.x + this.leftBorder, this.y + this.topBorder,
                this.spriteWidth - this.leftBorder - this.rightBorder, this.spriteHeight - this.topBorder - this.bottomBorder);
    }

    protected void updateSprite() { //Only override if the object has an animation. This will be a fsm
//        if (this.animationCol < this.animationMaxCol) {
//            this.animationCol++;
//        } else {
//            this.animationCol = 0;
//        }
    }

    /////////////////////////////////////////
    /*----------GETTERS AND SETTERS--------*/
    /////////////////////////////////////////
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    protected int getWidth() {
        return this.spriteWidth;
    }

    protected int getHeight() {
        return this.spriteHeight;
    }

    protected abstract void loadSpriteSheet(BufferedImage image);

}

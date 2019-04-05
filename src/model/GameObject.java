package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {


    protected int x,y;
    protected boolean isSolid = true;
    protected boolean movable = true;
    protected int spriteWidth, spriteHeight; //This is the full width/height including borders
    protected SpriteSheet spriteSheet;
    //This is the border around where the sprite actually starts and spriteWidth / Height. To get better collisions.
    protected int leftBorder = 0;
    protected int rightBorder = 0;
    protected int topBorder = 0;
    protected int bottomBorder = 0;

    //This is updated in updateSprite for animations
    protected int animationRow = 0;
    protected int animationCol = 0;
    protected int animationMaxRow = 0;
    protected int animationMaxCol = 0;

    protected Image jfxImage; //To draw onto the canvas


    public GameObject(int xLocation, int yLocation, boolean scale, BufferedImage spriteSheet, int spriteSheetWidth, int spriteSheetHeight) {
        if (scale) {
            //Multiply by Floor width/height to scale up to real map size
            this.x = xLocation * Floor.getWidth();
            this.y = yLocation * Floor.getHeight();
        } else {
            this.x = xLocation;
            this.y = yLocation;
        }
        this.spriteWidth = spriteSheetWidth;
        this.spriteHeight = spriteSheetHeight;
        this.loadSpriteSheet(spriteSheet);
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

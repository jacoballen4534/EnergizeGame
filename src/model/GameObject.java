package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {


    protected int x,y;
    protected boolean isSolid = true;
    protected boolean movable = true;
    protected int width = 32, height = 32; //size of sprite
    protected SpriteSheet spriteSheet;
    protected int leftBorder = 0;
    protected int rightBorder = 0;
    protected int topBorder = 0;
    protected int bottomBorder = 0;

    public GameObject(int x, int y, boolean scale, BufferedImage image) { //Scale is true when loading from map image
        if (scale) {
            this.x = x * this.width;
            this.y = y * this.height;
        } else {
            this.x = x;
            this.y = y;
        }
        this.loadSpriteSheet(image);
    }


    public abstract void tick();
    public abstract void render(GraphicsContext graphicsContext);
    protected Rectangle getBounds() {
        return new Rectangle(this.x + this.leftBorder, this.y + this.topBorder,
                this.width - this.leftBorder - this.rightBorder, this.height - this.topBorder - this.bottomBorder);
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
        return this.width;
    }

    protected int getHeight() {
        return this.height;
    }

    protected abstract void loadSpriteSheet(BufferedImage image);

}

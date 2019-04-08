package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {


    protected int x,y;
    protected boolean isSolid = true; //For collisions.
    protected int spriteWidth, spriteHeight; //Full width/height including any borders to setup sprite sheet
    protected SpriteSheet spriteSheet; //The full sheet, able to get subsections to draw

    //The difference between where the sprite actually starts and spriteWidth / Height. Used to improve collisions.
    protected int leftBorder = 0;
    protected int rightBorder = 0;
    protected int topBorder = 0;
    protected int bottomBorder = 0;

    // Used to cycle through the appropriate sprites for the desired animation
    protected int animationRow = 0;
    protected int animationCol = 0;
    protected int animationMaxRow = 0;
    protected int animationMaxCol = 0;

    protected int spriteDirection = 1; //This is used to flip the image, -1 is face left, 1 is face right.
    protected Image jfxImage; //The sprite To draw onto the canvas. Needs to be in FXImage form.


    public GameObject(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight) {
        //Multiply by PIXEL_UPSCALE to scale up from single pixels to desired map size
        this.x = xLocation * Game.PIXEL_UPSCALE;
        this.y = yLocation * Game.PIXEL_UPSCALE;
        //Create the sprite sheet with the actual size of the image.
        this.spriteWidth = spriteSheetWidth;
        this.spriteHeight = spriteSheetHeight;
        this.loadSpriteSheet(spriteSheet);
        //Update the sprite width and height to the desired render size to allow up/down scaling.
        this.spriteWidth = renderWidth;
        this.spriteHeight = renderHeight;
    }


    public abstract void tick();
    public abstract void render(GraphicsContext graphicsContext);
    protected Rectangle getBounds() {
        //As the full sprite includes empty space on the sides, Shrink the bounds by the specific border to get the actual bounds.
        return new Rectangle(this.x + this.leftBorder, this.y + this.topBorder,
                this.spriteWidth - this.leftBorder - this.rightBorder, this.spriteHeight - this.topBorder - this.bottomBorder);
    }

    protected void updateSprite() { //Updates to the next sprite for the appropriate animation.

    }

    protected void loadSpriteSheet(BufferedImage image) {
        //Pass in physical width and height of each sprite
        this.spriteSheet = new SpriteSheet(image, this.spriteWidth, this.spriteHeight);
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
}

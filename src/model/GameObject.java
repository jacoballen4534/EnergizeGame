package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {


    protected double x, y;
    protected boolean isSolid = true; //For collisions.
    protected int spriteWidth, spriteHeight; //Full width/height including any borders to setup sprite sheet
    protected SpriteSheet spriteSheet; //The full sheet, able to get subsections to draw

    //The difference between where the sprite actually starts and spriteWidth / Height. Used to improve collisions.
    protected AnimationsState animationsState = new AnimationsState();
    protected int currentAnimationCol = 0;
    // Used to cycle through the appropriate sprites for the desired animation


    protected int spriteDirection = 1; //This is used to flip the image, -1 is face left, 1 is face right.
    protected Image jfxImage; //The sprite To draw onto the canvas. Needs to be in FXImage form.


    //TODO: Add isSolid to construcot, and pass empty rectangle in getBounds if it is not solid
    public GameObject(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight) {
        //Multiply by PIXEL_UPSCALE to scale up from single pixels to desired map size
        this.x = xLocation * Game.PIXEL_UPSCALE;
        this.y = yLocation * Game.PIXEL_UPSCALE;
        //Create the sprite sheet with the actual size of the image.
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.loadSpriteSheet(spriteSheet);
        //Update the sprite width and height to the desired render size to allow up/down scaling.
        this.spriteWidth = renderWidth;
        this.spriteHeight = renderHeight;
    }


    public void tick(double cameraX, double cameraY){
    }

    public abstract void render(GraphicsContext graphicsContext, double cameraX, double cameraY);

    protected Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y,
                (int)(this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder()),
                (int)(this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()));
    }

    protected void renderBoundingBox(GraphicsContext graphicsContext) {
        graphicsContext.setFill(new Color(0.5, 0.5, 0.5, 0.5));
        graphicsContext.fillRect((int)this.x, (int)(this.y),
                (int)(this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder()),
                (int)(this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()));
    }

    protected void renderAttackBoundingBox(GraphicsContext graphicsContext) {
        graphicsContext.setFill(new Color(0.9, 0.2, 0.2, 0.5));

        if (this.spriteDirection == 1) {
            graphicsContext.fillRect((int) this.x, (int) (this.y - 10),
                    (int) (this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder() + 48),
                    (int) (this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()) + 20);
        } else {
            graphicsContext.fillRect((int) this.x - 48, (int) (this.y - 10),
                    (int) (this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder() + 48),
                    (int) (this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()) + 20);
        }
    }

    protected void updateSprite() { //Updates to the next sprite for the appropriate animation.
    }

    protected boolean inCameraBounds(double cameraX, double cameraY) {
        return (this.x + this.spriteWidth > cameraX && this.x < cameraX + Game.SCREEN_WIDTH && this.y + this.spriteHeight > cameraY && this.y < cameraY + Game.SCREEN_HEIGHT);
    }

    protected void loadSpriteSheet(BufferedImage image) {
        //Pass in physical width and height of each sprite
        this.spriteSheet = new SpriteSheet(image, this.spriteWidth, this.spriteHeight);
    }

    /////////////////////////////////////////
    /*----------GETTERS AND SETTERS--------*/
    /////////////////////////////////////////
    public double getX() {
        return this.x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return this.y;
    }
    public void setY(double y) {
        this.y = y;
    }
    protected int getWidth() {
        return this.spriteWidth;
    }
    protected int getHeight() {
        return this.spriteHeight;
    }
}

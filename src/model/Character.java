package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

public abstract class Character extends GameObject{
    private String name;
    protected int health;
    protected Weapon weapon;
    protected float velocityX = 0, velocityY = 0;
    //To play each animation once.
   protected boolean playGotAttackedAnimation = false;
   protected boolean playAttackAnimation = false;
   protected boolean playDieAnimation = false;
   private int levelWidth; //This is in pixels, Used to check tiles in a 2 tile radius


    public Character(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight, int renderWidth, int rederHeight, int levelWidth) {
        super(xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight, renderWidth, rederHeight);
        this.levelWidth = levelWidth;

    }


    @Override
    protected void updateSprite() {

        //Update the direction, Do this first so vertical movement gets the updated bounding box
        if (this.velocityX > 0) {//right
            this.spriteDirection = 1;
        } else if (this.velocityX < 0) {//left
            this.spriteDirection = -1;
        } //other case is idle so leave the direction how it how it was before

        this.updateAnimationState();

        if (this.spriteDirection == -1) { //If the character is facing left, swap the left and right borders
            this.animationsState.flipBoundingBoxX();
        }

        this.currentAnimationCol = this.animationsState.updateAnimationSprite(this.currentAnimationCol);
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.currentAnimationCol,this.animationsState.getAnimationRow()), null);

    }

    abstract void updateAnimationState();
    abstract void attack();
    abstract void playSound();
    abstract void getHit();

    /////////////////////////////////////////
    /*----------GETTERS AND SETTERS--------*/
    /////////////////////////////////////////
    protected String getName() {
        return name;
    }
    protected float getVelocityX() {
        return velocityX;
    }
    protected void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }
    protected float getVelocityY() {
        return velocityY;
    }
    protected void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public int getLevelWidth() {
        return this.levelWidth;
    }

    public void tick(double cameraX, double cameraY) {//Update x and y secretly to allow sliding
        //Turn around if protagonist has collided with something
        this.x += this.velocityX;
        if (Handler.checkCollision(this, cameraX, cameraY)) {
            this.x += this.velocityX * -1.02;
        }

        this.y += this.velocityY;
        if (Handler.checkCollision(this, cameraX, cameraY)) {
            this.y += this.velocityY * -1;
        }
    }


    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {

        if (this.inCameraBounds(cameraX,cameraY)) {
            if (this.spriteDirection == 1) { //facing right
                graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
            } else {
                graphicsContext.drawImage(this.jfxImage, this.x + this.spriteWidth, this.y, -this.spriteWidth, this.spriteHeight);
            }
//            this.renderBoundingBox(graphicsContext);
        }
    }
}

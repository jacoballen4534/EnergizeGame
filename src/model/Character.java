package model;

import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public abstract class Character extends GameObject{
    private String name;
    protected int health;
    protected Weapon weapon;
    protected float velocityX = 0, velocityY = 0;
    protected boolean attacking = false;


    public Character(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight, int renderWidth, int rederHeight) {
        super(xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight, renderWidth, rederHeight);
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
}

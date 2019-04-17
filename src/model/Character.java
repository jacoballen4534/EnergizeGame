package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Character extends GameObject{
    private String name;
    protected int currHealth;
    protected int maxHealth;
    protected Weapon weapon;
    protected float velocityX = 0, velocityY = 0;
    //To play each animation once.
    protected boolean playGotAttackedAnimation = false;
    protected boolean playAttackAnimation = false;
    protected boolean playDieAnimation = false;
    private int levelWidth; //This is in pixels, Used to check tiles in a 2 tile radius
    protected int attackDamage = 1; //initialize with 1 but set in each constructor. Vary based on enemy type and weapon type



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

    protected void attack() {
        if (!this.playAttackAnimation) { //Only restart the animation the first time. Can only attack once previous attack has finished
            this.currentAnimationCol = 0; //To start the animation from the start.
            this.playAttackAnimation = true; //Indicate to start playing the attack animation once.
        }
    }

    protected void getHit(int damage) {
        if (!this.playGotAttackedAnimation) {
            System.out.println("Character got hit");
            this.currentAnimationCol = 0;//To start the animation from the start.
            this.playGotAttackedAnimation = true;
            this.currHealth -= damage;
            if (this.currHealth < 0) this.currHealth = 0;
        }
    }


    protected Rectangle getAttackBounds() {
        if (this.spriteDirection == 1) {
            return new Rectangle((int) this.x, (int) (this.y - 10),
                    (int) (this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder() + 40),
                    (int) (this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()) + 20);
        } else {
            return new Rectangle((int) this.x - 48, (int) (this.y - 10),
                    (int) (this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder() + 48),
                    (int) (this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()) + 20);
        }
    }

    /*protected void updateTarget(Character target) {
        //Empty for Protagonist, override in enemy
    }*/

    protected int getAttackDamage() { //To vary the amount of damage dealt with better weapons
        return this.attackDamage;
    }

    public void tick(double cameraX, double cameraY) {//Update x and y separately to allow sliding
        //Move 1 pixel at a time until reaching the destination or colliding with something.
        //If this slows down the game, revert to old version below.
        double directionX = this.velocityX / Math.abs(this.velocityX);
        double directionY = this.velocityY / Math.abs(this.velocityY);

        for (int x = 0; x < Math.abs(this.velocityX); x++) {
            this.x += directionX;
            if (Handler.checkCollision(this, cameraX, cameraY)) {
                this.x -= directionX;
                break;
            }
        }

        for (int y = 0; y < Math.abs(this.velocityY); y++) {
            this.y += directionY;
            if (Handler.checkCollision(this, cameraX, cameraY)) {
                this.y -= directionY;
                break;
            }
        }

        //Turn around if protagonist has collided with something
//        if (this.x != 0) { //Dont need to move or check collisions if it inst moving in that direction.
//            this.x += this.velocityX;
//            if (Handler.checkCollision(this, cameraX, cameraY)) {
//                this.x += this.velocityX * -1.02;
//            }
//        }
//
//        if (this.y != 0) { //Dont need to move or check collisions if it inst moving in that direction.
//            this.y += this.velocityY;
//            if (Handler.checkCollision(this, cameraX, cameraY)) {
//                this.y += this.velocityY * -1;
//            }
//        }
    }


    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {

        if (this.inCameraBounds(cameraX,cameraY)) {
            if (this.spriteDirection == 1) { //facing right
                graphicsContext.drawImage(this.jfxImage, this.x - this.animationsState.getLeftBorder(), this.y - this.animationsState.getTopBorder(), this.spriteWidth, this.spriteHeight);
            } else {
                graphicsContext.drawImage(this.jfxImage, this.x + this.spriteWidth - this.animationsState.getLeftBorder(),
                        this.y - this.animationsState.getTopBorder(), -this.spriteWidth, this.spriteHeight);
            }
            this.renderBoundingBox(graphicsContext);
        }
    }

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

    public void updateLevelWidth(int newLevelWidth) {
        this.levelWidth = newLevelWidth;
    }
}
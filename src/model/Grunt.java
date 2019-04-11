package model;

import javafx.embed.swing.SwingFXUtils;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grunt extends Enemy {

    private AnimationsState walkState;
    private AnimationsState idleState;
    private AnimationsState getHitState;
    private AnimationsState dieState;
    private AnimationsState attackState;
    private AnimationsState alertState;

    public Grunt(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, Character target, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, target, levelWidth);
        //TODO: Add borders and additional sprite sheets
        this.attackState = new AnimationsState(9,54,16,1,17, 0,0);
        this.dieState = new AnimationsState(0,0,0,0,14, 1,0); //Doesnt need a border
        this.walkState = new AnimationsState(0,72,15,0,12, 2,0);
        this.idleState = new AnimationsState(0,63,15,0,10, 3,0);
        this.getHitState = new AnimationsState(0,45,15,0,7, 4,0);
        this.alertState = new AnimationsState(0,63,15,0,3, 4,0);

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }

    @Override
    void updateAnimationState() {
        if (this.playDieAnimation) {
            this.animationsState.copy(this.dieState);
//            this.spriteSheet =
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playDieAnimation = false; //Once the animation has finished, set this to false to only play the animation once
                //Remove from handler.
            }
        }else if (this.playGotAttackedAnimation) { //Got Hit
            this.animationsState.copy(this.getHitState);
//            this.spriteSheet =
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playGotAttackedAnimation = false;
            }
        } else if (this.playAttackAnimation) {
            this.animationsState.copy(this.attackState);
//            this.spriteSheet =
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playAttackAnimation = false;
            }
        } else if (this.velocityX == 0 && this.velocityY == 0) { //Idle
            this.animationsState.copy(this.idleState);
//            this.spriteSheet =
        } else { //Walking
            this.animationsState.copy(walkState);
//            this.spriteSheet =
        }
    }

    public void tick(double cameraX, double cameraY) {
        if(this.playGotAttackedAnimation || this.playDieAnimation || this.playAttackAnimation) {
            this.velocityX = 0;
            this.velocityY = 0;
        } else {
            //Give a 5% chance of changing each direction
            if (Game.getNextRandomInt(100) > 94) { //Use 94 as it is from 0 to 99
                this.velocityX = (Game.getNextRandomInt(3) - 1) * 3; //Random number 0,1 or 2. Shift and scale to get either -5,0,5
            }

            if (Game.getNextRandomInt(100) > 94) {
                this.velocityY = (Game.getNextRandomInt(3) - 1) * 3;
            }
        }
        
        super.tick(cameraX,cameraY);

    }

    @Override
    void isPlayerInSight() {

    }

    @Override
    void findPlayer() {

    }

    @Override
    protected void attack() {
        this.animationsState.copy(this.attackState); //Set the state to update the bounding boxes
        super.attack();
        Handler.attack(this);
    }

    @Override
    void playSound() {

    }

    @Override
    protected void getHit() {
        this.animationsState.copy(this.getHitState);
        super.getHit();
        if (this.currHealth <= 0) { //died
            this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
        }
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

}

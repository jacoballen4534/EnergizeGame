package model;

import javafx.animation.Animation;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Grunt extends Enemy {

    private AnimationsState walkState;
    private AnimationsState idleState;
    private AnimationsState getHitState;
    private AnimationsState deadState;
    private AnimationsState attackState;


    public Grunt(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, Character target, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, target, levelWidth);
        //TODO: Add borders
        this.walkState = new AnimationsState(0,0,0,0,12, 0,0);
        this.idleState = new AnimationsState();
        this.getHitState = new AnimationsState();
        this.deadState = new AnimationsState();
        this.attackState = new AnimationsState();

        //Add extra sprite sheets
//        this.spriteSheet = new SpriteSheet(preloadedim., hard code actual Width, Hard code actual height);

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }


    @Override
    void updateAnimationState() {
        //TODO: Update the spritesheet
        if (this.playDieAnimation) {
            this.animationsState.copy(this.deadState);
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
        } else if (this.playAttckAnimation) {
            this.animationsState.copy(this.attackState);
//            this.spriteSheet =
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playAttckAnimation = false;
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
        //Give a 5% chance of changing each direction
        if (Game.getNextRandomInt(100) > 94) { //Use 94 as it is from 0 to 99
            this.velocityX = (Game.getNextRandomInt(3) - 1) * 2; //Random number 0,1 or 2. Shift and scale to get either -5,0,5
        }

        if (Game.getNextRandomInt(100) > 94) {
            this.velocityY = (Game.getNextRandomInt(3) - 1) * 2;
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
    void attack() {
        this.playAttckAnimation = true;

    }

    @Override
    void playSound() {

    }

    @Override
    void getHit() {
        this.playGotAttackedAnimation = true;
        if (this.health <= 0) { //died
            this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
        }
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

}

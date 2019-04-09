package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Protagonist extends Character {
    private static int nextID; //Unique id for all characters, this will be used for multiplayer
    protected int id;
    private int lives;
    private KeyInput keyInput;
    private boolean buttonAllreadyDown = false;
    private AnimationsState runningState;
    private AnimationsState idleState;
    private AnimationsState attackState;

    //    public Inventory inventory;

    public Protagonist(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, KeyInput keyInput, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
        this.id = nextID++;
        this.keyInput = keyInput;

        this.idleState = new AnimationsState(45,48,17, 5, 3, 0, 0);
        this.runningState = new AnimationsState(52,38,20,5, 6, 1, 1);
//        this.attackState = new AnimationsState();

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
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
    void updateAnimationState() {
        //IMPLICIT PRIORITY. ORDER = DIE, ATTACKING, GotHit, IDLE/RUNNING
        //After die animation last frame, fade out ...Gmae over
        if (this.playAttckAnimation) { //Attacking
            //Update attack animation
            this.animationsState.copy(attackState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playGotAttackedAnimation = false; //Once the animation has finished, set this to false to only play the animation once
            }
        } else if (this.velocityX == 0 && this.velocityY == 0) { //Idle
            //Update bounding box
            this.animationsState.copy(this.idleState);
        } else { //Running
            this.animationsState.copy(runningState);
        }
    }


    public void tick(double cameraX, double cameraY) {
        if (this.keyInput.up) this.velocityY = -5;
        else if(!this.keyInput.down) this.velocityY = 0;

        if (this.keyInput.down) this.velocityY = 5;
        else if(!this.keyInput.up) this.velocityY = 0;

        if (this.keyInput.right) {
            this.velocityX = 5;
            if (!this.buttonAllreadyDown) {
                this.updateSprite();
                this.buttonAllreadyDown = true;
            }

        } else if(!this.keyInput.left) {
            this.velocityX = 0;
            if (this.buttonAllreadyDown) {
                this.updateSprite();
                this.buttonAllreadyDown = false;
            }
        }

        if (this.keyInput.left) {
            this.velocityX = -5;
            if (!this.buttonAllreadyDown) {
                this.updateSprite();
                this.buttonAllreadyDown = true;
            }
        } else if(!this.keyInput.right) {
            this.velocityX = 0;
            if (this.buttonAllreadyDown) {
                this.updateSprite();
                this.buttonAllreadyDown = false;
            }
        }

        super.tick(cameraX,cameraY);

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    protected void GetHit(){
        System.out.println("I got hit!");
        this.playGotAttackedAnimation = true;
        if (this.health <= 0) { //loose life
            this.lives --;
            if (this.lives <= 0) { //died
                this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
            }
        }
    }

}


package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Protagonist extends Character {
    private static int nextID; //Unique id for all characters, this will be used for multilayer
    protected int id;
    private int lives; //Keep track of how many lives, Can pick up hearts which increase this. 0 = dead.
    //private KeyInput keyInput; //The keyboard inputs to move the character.
    private boolean buttonAlreadyDown = false; //To only update animation state on button initial press, not on hold.
    private boolean isAttacking = false; //Attempt to debounce attacking
    //The different animation states to hold the borders and which sprite from sprite sheet to use.
    private AnimationsState runningState;
    private AnimationsState idleState;
    private AnimationsState attackState;
    private int currEnergy;
    private int maxEnergy;
    private HUD hud;

    //    public Inventory inventory;

    public Protagonist(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
        this.id = nextID++; //Give each protagonist a unique id. (Will be used for multiplayer)
        //this.keyInput = keyInput;

        //Set up the bounding boxes and sprite selection for the different animation options.
        this.idleState = new AnimationsState(45,48,17, 5, 3, 0, 0);
        this.runningState = new AnimationsState(52,38,20,5, 6, 1, 1);
        this.attackState = new AnimationsState(45,48,17,5,6,6,0); //After attack button is setup

        //Set health
        this.currHealth = 100;
        this.currEnergy = 100;
        this.maxHealth = this.currHealth;
        this.maxEnergy = this.currEnergy;
        this.hud = new HUD(this.id, this.maxHealth,this.maxEnergy,200,100,50,50);
        hud.setHealth(this.currHealth);
        hud.setEnergy(this.currEnergy);

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }

    @Override
    protected void attack() {
        super.attack();
        //TODO: Actually attack.
        this.playAttackAnimation = true;
        hud.setEnergy(hud.getEnergy()-10);

    }

    @Override
    void playSound() {
        System.out.println("Beep");
    }

    @Override
    protected void getHit() {
        super.getHit();
        if (this.currHealth <= 0) { //died
            this.playGotAttackedAnimation = false;
            this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
        }
    }

    @Override
    void updateAnimationState() {
        //Determine what state the player is in, and update the animation accordingly.
        //IMPLICIT PRIORITY. ORDER = DIE, ATTACKING, GotHit, IDLE/RUNNING
        //After die animation last frame, fade out ...Game over
        if (this.playAttackAnimation) { //Attacking
            //Update attack animation
            this.animationsState.copy(attackState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playAttackAnimation = false; //Once the animation has finished, set this to false to only play the animation once
                //hud.setEnergy(hud.getEnergy()-10);
                this.isAttacking = false;
            }
        } else if (this.velocityX == 0 && this.velocityY == 0) { //Idle
            this.animationsState.copy(this.idleState);
        } else { //Running (As this is the only other option)
            this.animationsState.copy(runningState);
        }
    }


    public void tick(double cameraX, double cameraY, KeyInput keyInput) {
        //Update the velocity according to what keys are pressed.
        //If the key has just been pressed, update the animation. This leads to more responsive animations.

        if (keyInput.getKeyPress("up")){
            this.velocityY = -5;
            System.out.println("UP");
        }
        else if (keyInput.getKeyPress("down")){
            this.velocityY = 5;
            System.out.println("DOWN");
        }
        else this.velocityY=0;

        if (keyInput.getKeyPress("right")){
            this.velocityX = 5;
            System.out.println("RIGHT");
        }
        else if (keyInput.getKeyPress("left")) {
            this.velocityX = -5;
            System.out.println("LEFT");
        }
        else this.velocityX=0;

        if (keyInput.getKeyPress("attack")){
            if (hud.getEnergyPercent() > 0.5){
                if (!this.isAttacking) {
                    this.attack();
                    this.isAttacking = true;
                }
            }
            else{
                System.out.println("Not enough energy!");
            }
        }

        if (keyInput.getKeyPress("jump")){
            System.out.println("Jump for joy");// Using this to make it easier to custom add key bindings later
        }

        if (keyInput.getKeyPress("useItem")){
            System.out.println("Using an item");
        }

        if (keyInput.getKeyPress("useSpecial")){
            System.out.println("Azarath, metrion, zinthos!"); //Outdated reference
        }

        if (keyInput.getKeyPress("cheatKey")){
            System.out.println("Wow, cheating in 2019?");
            hud.setEnergy(this.maxEnergy);
        }

        if (keyInput.getKeyPress("inventory")){
            System.out.println("Opens inventory");
            //Handler.pauseGame();
            //inventory.show();
        }

        super.tick(cameraX,cameraY); //Check collisions and update x and y
        hud.tick(cameraX, cameraY); //Update health and energy displays

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    protected void GetHit(){
        System.out.println("I got hit!");
        this.playGotAttackedAnimation = true;
        if (this.currHealth <= 0) { //lose life
            this.lives--;
            if (this.lives <= 0) { //died
                this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
            }
        }
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
        super.render(graphicsContext, cameraX, cameraY);
        hud.render(graphicsContext, cameraX, cameraY);
    }

    public HUD getHud() {
        return hud;
    }
}


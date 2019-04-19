package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Protagonist extends Character {
    private static int nextID = 0; //Unique id for all characters, this will be used for multilayer
    protected int id;
    private int lives; //Keep track of how many lives, Can pick up hearts which increase this. 0 = dead.
    //private KeyInput keyInput; //The keyboard inputs to move the character.
    private boolean buttonAlreadyDown = false; //To only update animation state on button initial press, not on hold.
    private boolean isAttacking = false; //Attempt to debounce attacking
    private boolean playBlockingAnimation = false; //To add shield effect
    //The different animation states to hold the borders and which sprite from sprite sheet to use.
    private AnimationsState idleState;
    private AnimationsState blockingState;
    private long lastBlockTimer, blockCooldown, blockTimer = 0;


    private final int PROTAGONIST_MAXHEALTH = 100;
    private final int PROTAGONIST_MAXENERGY = 100;
    private final int PROTAGONIST_BASE_ATTACK_DAMAGE = 34;
    private final int PROTAGONIST_ATTACK_COOLDOWN = 1000;
    private final int PROTAGONIST_BLOCK_COOLDOWN = 1; //Change this to add block cool down for increased difficulty (ms).
    private final int BLOCK_COST = 5;

    private int currEnergy;
    private int maxEnergy;
    private HUD hud;

    private Item equippedItem;
    private Inventory inventory;
    private Image shield;


    public Protagonist(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
        this.id = nextID++; //Give each protagonist a unique id. (Will be used for multilayer)
        //this.keyInput = keyInput;

        //Set up the bounding boxes and sprite selection for the different animation options.
        this.idleState = new AnimationsState(45,45,17, 5, 3, 0, 0);
        this.runningState = new AnimationsState(52,38,20,5, 6, 1, 1);
//        this.attackState = new AnimationsState(45,0,0,5,6,6,0);
        this.attackState = new AnimationsState(45,45,17,5,6,6,0);
        this.gotHitState = new AnimationsState(45,45,17,5,6,9,0); //Place holder till get hit sprite
        this.blockingState = new AnimationsState(45,45,17,5,6,12,1);
        this.attackCooldown = PROTAGONIST_ATTACK_COOLDOWN;
        this.blockCooldown = PROTAGONIST_BLOCK_COOLDOWN;


        //Set health
        this.currHealth = PROTAGONIST_MAXHEALTH;
        this.currEnergy = 0; //Start with 0 energy and build it up
        this.maxHealth = PROTAGONIST_MAXHEALTH;
        this.maxEnergy = PROTAGONIST_MAXENERGY;
        this.hud = new HUD(this.id, this.maxHealth,this.maxEnergy,300,100,50,50);
        hud.setHealth(this.currHealth);
        hud.setEnergy(this.currEnergy);

        this.shield = SwingFXUtils.toFXImage(PreLoadedImages.shieldSpriteSheet, null);
        inventory = new Inventory(10);
        equippedItem = null;

        this.attackDamage = PROTAGONIST_BASE_ATTACK_DAMAGE; //Start with 10 damage pwe hit and updated based on weapon tier.

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }

    @Override
    protected void attack() {
        if(super.canAttack()) {
            Handler.attack(this);
        }
    }

    private void block() {
        this.blockTimer += System.currentTimeMillis() - this.lastBlockTimer;
        this.lastBlockTimer = System.currentTimeMillis();

        if (!this.playBlockingAnimation &&  (this.blockTimer >= this.blockCooldown)) { //May take this out
            if (this.currEnergy > BLOCK_COST) { //Only block if you have enough health
                this.currEnergy -= BLOCK_COST;
                this.hud.setEnergy(this.currEnergy);

                this.animationsState.copy(this.blockingState);
                System.out.println("An impenetrable defence");
                this.currentAnimationCol = 1; //To start the animation from the start.
                this.playBlockingAnimation = true;
            } else {
                System.out.println("Not enough energy to block");
            }
        }
    }



    @Override
    protected void getHit(int damage) {
        if (!this.playAttackAnimation && !this.playDieAnimation && !this.playGotAttackedAnimation && !this.playBlockingAnimation) { //Cant get hit while attacking but there is a cool down
            this.animationsState.copy(this.gotHitState);
            super.getHit(damage);
            this.hud.setHealth(this.currHealth);
            if (this.currHealth <= 0) { //died
                this.playGotAttackedAnimation = false;
                this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
            }
        }
    }

    @Override
    void updateAnimationState() {
        //Determine what state the player is in, and update the animation accordingly.
        //IMPLICIT PRIORITY. ORDER = DIE, ATTACKING, GotHit, IDLE/RUNNING
        //After die animation last frame, fade out ...Game over
        if (this.playBlockingAnimation){
            this.animationsState.copy(this.blockingState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playBlockingAnimation = false;
                this.blockTimer = 0;
            }
        }else if (this.playAttackAnimation) { //Attacking
            //Update attack animation
            this.animationsState.copy(this.attackState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playAttackAnimation = false; //Once the animation has finished, set this to false to only play the animation once
                this.isAttacking = false;
                this.attackTimer = 0;
            }
        } else if (this.playGotAttackedAnimation) {
            this.animationsState.copy(this.gotHitState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playGotAttackedAnimation = false; //Once the animation has finished, set this to false to only play the animation once
            }
        } else if (this.playDieAnimation) {
//            this.animationsState.copy(this.dieAnimation);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                Handler.removePlayer(this);
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
        if(this.playGotAttackedAnimation || this.playDieAnimation || this.playAttackAnimation || this.playBlockingAnimation) { //If the player is in an animation, disable movement
            this.velocityX = 0;
            this.velocityY = 0;
        } else {

            if (keyInput.getKeyPress("up")){
                this.velocityY = -5;
            } else if (keyInput.getKeyPress("down")){
                this.velocityY = 5;
            } else this.velocityY=0;

            if (keyInput.getKeyPress("right")) {
                this.velocityX = 5;
                //Update the sprite / bounding box before moving, to make sure the new animation bounding box isn't inside a wall.
                if (!this.buttonAlreadyDown) {
                    this.updateSprite();
                    this.buttonAlreadyDown = true;
                }
            } else if (keyInput.getKeyPress("left")) {
                this.velocityX = -5;
                //Update the sprite / bounding box before moving, to make sure the new animation bounding box isn't inside a wall.
                if (!this.buttonAlreadyDown) {
                    this.updateSprite();
                    this.buttonAlreadyDown = true;
                }
            } else {
                this.velocityX = 0;
                this.buttonAlreadyDown = false;
            }
        }

        if (keyInput.getKeyPressDebounced("attack")){
            this.attack();
        }

        if (keyInput.getKeyPressDebounced("jump")){
            System.out.println("Jump for joy");// Using this to make it easier to custom add key bindings later
        }

        if (keyInput.getKeyPressDebounced("useItem")){
            useItem();
        }

        if (keyInput.getKeyPress("block")){
            this.block();
        }

        if (keyInput.getKeyPressDebounced("useSpecial")){
            if (useSpecial()) {
                System.out.println("Azarath, metrion, zinthos!");//Outdated reference
            }  else System.out.println("Insufficient energy");
        }

        if (keyInput.getKeyPressDebounced("cheatKey")){
            System.out.println("Wow, cheating in 2019?");
            currEnergy = maxEnergy;
            hud.setEnergy(currEnergy);
            currHealth = maxHealth;
            hud.setHealth(currHealth);
        }

        super.tick(cameraX,cameraY); //Check collisions and update x and y
        hud.tick(cameraX, cameraY); //Update health and energy displays

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }


    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
        super.render(graphicsContext, cameraX, cameraY);
        if (this.playBlockingAnimation) {
//            graphicsContext.setFill(new Color(1,0.56, 0.31,0.5));
            double characterHeight = this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder();
            double characterWidth = this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder();

            if (this.spriteDirection == 1) {
                graphicsContext.drawImage(this.shield, this.x - 15, this.y + this.animationsState.getTopBorder()/3d , characterWidth * 2, characterHeight * 1.1);
//                graphicsContext.fillOval(this.x, this.y + this.animationsState.getTopBorder()/2 , characterWidth * 1.5, characterHeight);
            } else {
                graphicsContext.drawImage(this.shield,this.x + characterWidth + 15, this.y + this.animationsState.getTopBorder()/3, -characterWidth * 2, characterHeight * 1.1);
            }



        }
        hud.render(graphicsContext, cameraX, cameraY);
//        if (playAttackAnimation) {
//            this.renderAttackBoundingBox(graphicsContext);
//        }
    }

    public HUD getHud() {
        return hud;
    }

    public boolean pickUpItem(Item item){
        if (!this.inventory.isFull()){
            this.inventory.addItem(item);
            return true;
        }
        return false;
    }

    public void useItem(){
        if (equippedItem != null) {
            System.out.println("Using an item");
            equippedItem.useItem();
        } else {
            System.out.println("You dont have an item to use");
        }
    }

    public void setEquippedItem(Item item){
        equippedItem = item;
    }

    public void addEnergy(int amount) {
        this.currEnergy += amount;
        if (this.currEnergy > maxEnergy) { //Can't go over max
            this.currEnergy = maxEnergy;
        }
        this.hud.setEnergy(this.currEnergy);
    }

    public boolean useSpecial(){
        if (currEnergy == maxEnergy){
            currEnergy = 0; //Use all energy
            hud.setEnergy(currEnergy);
            return true;
        } else {
            return false;
        }
    }
}


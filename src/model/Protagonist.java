package model;

import Multiplayer.Client;
import Multiplayer.Server;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Protagonist extends Character {
    protected static int nextID = 0; //Unique id for all characters, this will be used for multilayer
    protected int id;
    protected int lives; //Keep track of how many lives, Can pick up hearts which increase this. 0 = dead.
    //private KeyInput keyInput; //The keyboard inputs to move the character.
    protected boolean buttonAlreadyDown = false; //To only update animation state on button initial press, not on hold.
    protected boolean isAttacking = false; //Attempt to debounce attacking
    protected boolean playBlockingAnimation = false; //To add shield effect
    //The different animation states to hold the borders and which sprite from sprite sheet to use.
    protected AnimationsState blockingState;
    protected long lastBlockTimer, blockCooldown, blockTimer = 0;


    protected final int PROTAGONIST_MAXHEALTH = 100;
    protected final int PROTAGONIST_MAXENERGY = 100;
    protected final int PROTAGONIST_BASE_ATTACK_DAMAGE = 34;
    protected final int PROTAGONIST_ATTACK_COOLDOWN = 1000;
    protected final int PROTAGONIST_BLOCK_COOLDOWN = 1; //Change this to add block cool down for increased difficulty (ms).
    protected final int BLOCK_COST = 5;

    protected int currEnergy;
    protected int maxEnergy;
    protected HUD hud;

    protected int levelNumber;
    protected Inventory inventory;
    protected Image shield;
    private Client client = null;//This is how the protagonist talks to the server


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
        this.inventory = new Inventory(3);
        this.inventory.setEquippedItem(null);

        this.attackDamage = PROTAGONIST_BASE_ATTACK_DAMAGE; //Start with 10 damage pwe hit and updated based on weapon tier.

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }

    @Override
    protected void attack() {
        if(super.canAttack()) {
            Handler.attack(this);
        }
    }

    @Override
    protected boolean pickup(Item pickup) {
        if (!this.inventory.isFull()){
            if (this.inventory.getEquippedItem() == null){
                this.inventory.setEquippedItem(pickup);
            } else {
                this.inventory.addItem(pickup);
                System.out.println("Picked up item");
            }
            return true;
        }
        return false;
    }

    protected void block() {
        this.blockTimer += System.currentTimeMillis() - this.lastBlockTimer;
        this.lastBlockTimer = System.currentTimeMillis();

        if (!this.playBlockingAnimation &&  (this.blockTimer >= this.blockCooldown)) { //May take this out
            if (this.currEnergy > BLOCK_COST) { //Only block if you have enough energy
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
        if (!this.playDieAnimation && !this.playGotAttackedAnimation && !this.playBlockingAnimation) { //Cant get hit while attacking but there is a cool down

            //getting hit interrupts an enemy attack. To not make it interrupt and continue after getting hit, dont set these. Dont not be able to take damage while attacking, check above
//            this.playAttackAnimation = false;

            this.animationsState.copy(this.gotHitState);
            super.getHit(damage);
            this.hud.setHealth(this.currHealth);
            if (this.currHealth <= 0) { //died
                this.playGotAttackedAnimation = false;
                this.playAttackAnimation = false;
                this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
            }
        }
    }

    @Override
    protected void updateAnimationState() {
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
                this.keepRendering = false;
                Handler.removePlayer(this);
            }
        } else if (this.velocityX == 0 && this.velocityY == 0) { //Idle
            this.animationsState.copy(this.idleState);
        } else { //Running (As this is the only other option)
            this.animationsState.copy(runningState);
        }
    }


    public void increaseHealth(int amount) {
        this.currHealth += amount;
        if (this.currHealth > this.maxHealth) {
            this.currHealth = this.maxHealth;
        }
        this.hud.setHealth(this.currHealth);
    }

    public void increaseEnergy(int amount) {
        this.currEnergy += amount;
        if (this.currEnergy > this.maxEnergy) {
            this.currEnergy = this.maxEnergy;
        }
        this.hud.setEnergy(this.currEnergy);
    }

    protected void tick(double cameraX, double cameraY, String commands) {
        super.tick(cameraX,cameraY);
    }

    protected void tick(double cameraX, double cameraY, KeyInput keyInput) {
        String commands = "";
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
            commands += (Server.PACKET_ATTACK);
            this.attack();
        }


        if (keyInput.getKeyPressDebounced("useItem")){
            if (this.inventory.getEquippedItem() != null) {
                switch (this.inventory.getEquippedItem().name){
                    case "Health Kit":
                        commands += (Server.PACKET_USE_HEALTH);
                        break;
                    case "Energy Kit":
                        commands += (Server.PACKET_USE_ENERGY);
                        break;
                    case "Fire Scroll":
                        commands += (Server.PACKET_USE_FIRE);
                        break;
                    case "Ice Scroll":
                        commands += (Server.PACKET_USE_ICE);
                        break;
                    case "Wind Scroll":
                        commands += (Server.PACKET_USE_WIND);
                        break;
                }
            }

            useItem();
        }

        if (keyInput.getKeyPress("block")){
            commands += (Server.PACKET_BLOCK);
            this.block();
        }


        if (keyInput.getKeyPressDebounced("cheatKey")){
            commands += (Server.PACKET_CHEAT);
            this.cheat();

        }


        super.tick(cameraX,cameraY); //Check collisions and update x and y
        hud.tick(cameraX, cameraY); //Update health and energy displays

        if (this.client != null) { //Multi player
            commands = Server.PACKET_PROTAGONIST_UPDATE + Server.PACKET_ID + this.client.getClientID() + Server.PACKET_LEVEL_NUMBER + this.levelNumber + Server.PACKET_POSITION + this.x + "," + this.y + Server.PACKET_POSITION + commands;
            commands += Server.PACKET_END;
            sendToServer(commands);
        }
    }

    public boolean sendToServer(String message) {
        if (this.client != null) {
            this.client.send(message.getBytes());
            return true;
        }
        return false;
    }

    public void disconnect() {
        if (this.client != null) {
            this.client.disconnect();
        }
    }

    public void addClient(Client client) {
        this.client = client;
        this.id = client.getClientID();
    }

    public void setId(int id) {
        this.id = id;
    }

    protected void cheat() {
        currEnergy = maxEnergy;
        hud.setEnergy(currEnergy);
        currHealth = maxHealth;
        hud.setHealth(currHealth);
        for (int i = 0; i < 2; i++) {
            this.inventory.addItem(new Scroll("Fire Scroll", Level.SCROLL_DESCRIPTION, 0, 0,
                    PreLoadedImages.fireScrollSprite, Level.SCROLL_SPRITE_WIDTH, Level.SCROLL_SPRITE_HEIGHT));
            this.inventory.addItem(new Scroll("Wind Scroll", Level.SCROLL_DESCRIPTION, 0, 0,
                    PreLoadedImages.windScrollSprite, Level.SCROLL_SPRITE_WIDTH, Level.SCROLL_SPRITE_HEIGHT));
            this.inventory.addItem(new Scroll("Ice Scroll", Level.SCROLL_DESCRIPTION, 0, 0,
                    PreLoadedImages.iceScrollSprite, Level.SCROLL_SPRITE_WIDTH, Level.SCROLL_SPRITE_HEIGHT));
            this.inventory.addItem(new Pickup("Health Kit", Level.HEALTH_KIT_DESCRIPTION, 0,0,
                    PreLoadedImages.healthPickupSprite,Level.PICKUP_SPRITE_WIDTH,Level.PICKUP_SPRITE_HEIGHT));
            this.inventory.addItem(new Pickup("Energy Kit", Level.ENERGY_KIT_DESCRIPTION, 0,0,
                    PreLoadedImages.energyPickupSprite,Level.PICKUP_SPRITE_WIDTH,Level.PICKUP_SPRITE_HEIGHT));
        }

        //TODO: Give the player a bunch of items and spells
        Platform.runLater(() -> { //Teleports the player to the boss room
            Level bossLevel = Map.getBossLevel();
            Handler.loadBossRoom();

            this.x = (bossLevel.getDoors().get(TileType.DOOR_LEFT).getX() + 1) * Game.PIXEL_UPSCALE;
            this.y = bossLevel.getDoors().get(TileType.DOOR_LEFT).getY() * Game.PIXEL_UPSCALE;
        });
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public String updateTimer() {//This gets called each second
        return this.hud.updateTimer();
    }

    protected boolean isProtagonist(){
        return true;
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

    public void useItem(){
        if (this.inventory.getEquippedItem() != null) {
            this.inventory.getEquippedItem().useItem(this);
            if (inventory.getItemCount() > 0) {
                this.inventory.setEquippedItem(this.inventory.getItemList().get(0));
                this.inventory.removeItem(this.inventory.getEquippedItem());

            } else {
                this.inventory.setEquippedItem(null);
            }
            //Update inv
        } else {
            System.out.println("You don't have an item to use");
        }
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

    public void updateLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    public Inventory getInventory() {
        return inventory;
    }

}


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
    private final int GRUNT_BASE_ATTACK_DAMAGE = 10;
    private final int GRUNT_MAXHEALTH = 100;

    public Grunt(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth, boolean enabled) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight,levelWidth,enabled);
        //TODO: Add borders and additional sprite sheets
        //////////////////////////// SET UP ANIMATION STATES ////////////////////////////////
        this.attackState = new AnimationsState(9,54,16,1,17, 0,0);
        this.dieState = new AnimationsState(0,0,15,0,14, 1,0); //Doesnt need a border
        this.walkState = new AnimationsState(0,72,15,0,12, 2,0);
        this.idleState = new AnimationsState(0,63,15,0,10, 3,0);
        this.getHitState = new AnimationsState(0,45,15,0,7, 4,0);
//        this.getHitState = new AnimationsState(45,45,15,0,7, 4,0);
        this.alertState = new AnimationsState(0,63,15,0,3, 4,0);

        //////////////////////////// SET UP HEALTH / DAMAGE //////////////////////////////
        this.attackDamage = GRUNT_BASE_ATTACK_DAMAGE;
        this.currHealth = GRUNT_MAXHEALTH;

        //Initialise image for first animation
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null);
    }

    @Override
    void updateAnimationState() {
        if (this.playDieAnimation) {
            this.animationsState.copy(this.dieState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playDieAnimation = false; //Once the animation has finished, set this to false to only play the animation once
                Handler.removeEnemy(this);
            }
        }else if (this.playGotAttackedAnimation) { //Got Hit
            this.animationsState.copy(this.getHitState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playGotAttackedAnimation = false;
            }
        } else if (this.playAttackAnimation) {
            this.animationsState.copy(this.attackState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playAttackAnimation = false;
            }
        } else if (this.velocityX == 0 && this.velocityY == 0) { //Idle
            this.animationsState.copy(this.idleState);
        } else { //Walking
            this.animationsState.copy(walkState);
        }
    }

    public void tick(double cameraX, double cameraY) {
        if(this.playGotAttackedAnimation || this.playDieAnimation || this.playAttackAnimation) {
            this.velocityX = 0;
            this.velocityY = 0;
        } else {
            //Give a 5% chance of changing each direction
            int oddsOfChangeXDirection = Game.getNextRandomInt();
            if (oddsOfChangeXDirection > 94) { //5% chance of changing direction
                this.velocityX = ((Game.getNextRandomInt() % 3) - 1) * 3; //Random number 0,1 or 2. Shift and scale to get either -5,0,5
            }

            int oddsOfChangeYDirection = Game.getNextRandomInt();
            if (oddsOfChangeYDirection > 94) {
                this.velocityY = ((Game.getNextRandomInt()  % 3) - 1) * 3;
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
    protected void getHit(int damage) {
        this.animationsState.copy(this.getHitState);
        super.getHit(damage);
        if (this.currHealth <= 0) { //died
            if (!this.playDieAnimation) {
                //Make bounding box 0
                this.target.addEnergy(10);
            }
            this.playGotAttackedAnimation = false;
            this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
        }
    }

    @Override
    public Rectangle getBounds() {
        if (this.playDieAnimation) {
            return new Rectangle(0,0,0,0);
        } else {
            return super.getBounds();
        }
    }

}

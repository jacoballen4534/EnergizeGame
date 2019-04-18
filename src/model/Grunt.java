package model;

import javafx.embed.swing.SwingFXUtils;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grunt extends Enemy {

    private AnimationsState idleState;
    private AnimationsState dieState;

    private final int GRUNT_BASE_ATTACK_DAMAGE = 10;
    private final int GRUNT_MAXHEALTH = 100;
    private final int GRUNT_ATTACK_COOLDOWN = 2000;
    private final int GRUNT_APPLY_DAMEAGE_COL = 7;//To apply damage part way through the attack animation

    public Grunt(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth, boolean enabled) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight,levelWidth,enabled);
        //TODO: Add borders and additional sprite sheets
        //////////////////////////// SET UP ANIMATION STATES ////////////////////////////////
//        this.attackState = new AnimationsState(9,63,15,0,17, 0,0);
        this.attackState = new AnimationsState(9,54,16,0,17, 0,0);
        this.dieState = new AnimationsState(9,63,15,0,14, 1,0); //Doesnt need a border
        this.runningState = new AnimationsState(0,72,15,0,12, 2,0);
        this.idleState = new AnimationsState(0,63,15,0,10, 3,0);
        this.gotHitState = new AnimationsState(0,45,15,0,7, 4,0);
//        this.gotHitState = new AnimationsState(45,45,15,0,7, 4,0);
        this.attackCooldown = GRUNT_ATTACK_COOLDOWN;

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
            this.animationsState.copy(this.gotHitState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playGotAttackedAnimation = false;
            }
        } else if (this.playAttackAnimation) {
            this.animationsState.copy(this.attackState);
            if (this.currentAnimationCol == GRUNT_APPLY_DAMEAGE_COL) {
                Handler.attack(this); //TODO: Wait untill plart way through this animaiton before actually hitting
            } else if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playAttackAnimation = false;
                this.attackTimer = 0;
            }
        } else if (this.velocityX == 0 && this.velocityY == 0) { //Idle
            this.animationsState.copy(this.idleState);
        } else { //Walking
            this.animationsState.copy(runningState);
        }
    }

    protected void tick(double cameraX, double cameraY, Level level) {
        if(this.playGotAttackedAnimation || this.playDieAnimation || this.playAttackAnimation) {
            this.velocityX = 0;
            this.velocityY = 0;
        } else {
            int currentNodeId = (int)(this.x / Game.PIXEL_UPSCALE) + (int)(this.y / Game.PIXEL_UPSCALE) * level.getLevelWidth();
            int targetNodeId = (int)(this.target.getX() / Game.PIXEL_UPSCALE) + (int)(this.target.getY() / Game.PIXEL_UPSCALE) * level.getLevelWidth();

            //Give a 30% chance of changing each direction
            if (Game.getNextRandomInt(100, false) > 69) {
                int nextDirection = level.getShortestPath().nextDirection(currentNodeId, targetNodeId); //1=up,2=right,3=down,4=left
                switch (nextDirection) {
                    case 1: //up
                        this.velocityY = -3;
                        break;
                    case 2://right
                        this.velocityX = 3;
                        break;
                    case 3://down
                        this.velocityY = 3;
                        break;
                    case 4://left
                        this.velocityX = -3;
                        break;
                    case 5: //The path has not been found yet, so just move randomly
                        if (Game.getNextRandomInt(100, false) > 69) {
                            this.velocityX = Game.getNextRandomInt(100, false) > 49 ? 3 : -3;
                        } else {
                            this.velocityX = 0;
                        }
                        if (Game.getNextRandomInt(100, false) > 69) {
                            this.velocityY = Game.getNextRandomInt(100, false) > 49 ? 3 : -3;
                        } else {
                            this.velocityY = 0;
                        }
                }
            } else { //Give a tiny chance of getting a random direction to avoid getting stuck
                if (Game.getNextRandomInt(100, false) > 97) {
                    this.velocityX = Game.getNextRandomInt(100, false) > 49 ? 3 : -3;
                }
                if (Game.getNextRandomInt(100, false) > 97) {
                    this.velocityY = Game.getNextRandomInt(100, false) > 49 ? 3 : -3;
                }
            }
        }
        if (this.getAttackBounds().intersects(target.getBounds())) {
            this.attack();
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
       super.canAttack();
    }

    @Override
    protected void getHit(int damage) {
        if (!this.playDieAnimation && !this.playGotAttackedAnimation) {
            this.playAttackAnimation = false;//getting hit interrupts an enemy attack
            this.animationsState.copy(this.gotHitState);
            super.getHit(damage);
            if (this.currHealth <= 0) { //died
                //Make bounding box 0
                this.target.addEnergy(10);
                this.playGotAttackedAnimation = false;
                this.playDieAnimation = true; //Can leave other play animation booleans true as die has implicit priority when checking.
            }
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

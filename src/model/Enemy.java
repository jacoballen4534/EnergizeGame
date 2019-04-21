package model;

import javafx.scene.canvas.GraphicsContext;
import sample.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Character{
    protected Protagonist target; //For path finding.
    private boolean enabled;
    protected AnimationsState dieState;
    protected int APPLY_DAMEAGE_COL;
    protected int EnemyMovementSpeed;

    public Enemy(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth,
                 boolean enabled) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
        this.enabled = enabled;
    }

    protected void updateTarget(Protagonist target) {
        this.target = target;
    }

    @Override
    protected void updateAnimationState() {
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
            if (this.currentAnimationCol == APPLY_DAMEAGE_COL) {
                Handler.attack(this); //Wait until part way through this animation before actually hitting
            }
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playAttackAnimation = false;
                this.attackTimer = 0;
            }
        } else if (this.velocityX == 0 && this.velocityY == 0) { //Idle
            this.animationsState.copy(this.idleState);
        } else { //Walking
            this.animationsState.copy(runningState);
        }
    }

    protected void render(GraphicsContext graphicsContext, double cameraX, double cameraY) { //Here so the boss can overwride
        super.render(graphicsContext, cameraX, cameraY);
    }
    protected void tick(double cameraX, double cameraY, Level level) {
            int currentNodeId = (int)(this.x / Game.PIXEL_UPSCALE) + (int)(this.y / Game.PIXEL_UPSCALE) * level.getLevelWidth();
            int targetNodeId = (int)(this.target.getX() / Game.PIXEL_UPSCALE) + (int)(this.target.getY() / Game.PIXEL_UPSCALE) * level.getLevelWidth();

//            if (Game.getNextRandomInt(100, false) > 98) { //Can print out path periodically to show off path finding.
//                level.getShortestPath().findAndPrintPath(currentNodeId, targetNodeId);
//            }

            //Give a 50% chance of changing of getting a path update
            if (Game.getNextRandomInt() < 50) {
                int nextDirection = level.getShortestPath().nextDirection(currentNodeId, targetNodeId); //1=up,2=right,3=down,4=left
                switch (nextDirection) {
                    case 1: //up
                        this.velocityY = -this.EnemyMovementSpeed;
                        break;
                    case 2://right
                        this.velocityX = this.EnemyMovementSpeed;
                        break;
                    case 3://down
                        this.velocityY = this.EnemyMovementSpeed;
                        break;
                    case 4://left
                        this.velocityX = -this.EnemyMovementSpeed;
                        break;
                    case 5: //The path has not been found yet, so just move randomly
                        if (Game.getNextRandomInt() > 89) {
                            this.velocityX = Game.getNextRandomInt() > 49 ? this.EnemyMovementSpeed : -this.EnemyMovementSpeed;
                        }
                        if (Game.getNextRandomInt() > 89) {
                            this.velocityY = Game.getNextRandomInt() > 49 ? this.EnemyMovementSpeed : -this.EnemyMovementSpeed;
                        }
                }
            } else { //Give a tiny chance of getting a random direction to avoid getting stuck
                if (Game.getNextRandomInt() > 97) {
                    this.velocityX = Game.getNextRandomInt() > 49 ? 3 : -3;
                }
                if (Game.getNextRandomInt() > 97) {
                    this.velocityY = Game.getNextRandomInt() > 49 ? 3 : -3;
                }
            }
        super.tick(cameraX,cameraY);
    }

    @Override
    protected void getHit(int damage) {
        if (!this.playDieAnimation && !this.playGotAttackedAnimation) {

            //getting hit interrupts an enemy attack. To not make it interrupt and continue after getting hit, dont set these. Dont not be able to take damage while attacking, check above
            this.playAttackAnimation = false;
            this.playSpecialAttackAnimation = false;


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

    abstract void isPlayerInSight();
    abstract void findPlayer();

}

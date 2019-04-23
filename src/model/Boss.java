package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.Level.BOSS_SCALE;

public class Boss extends Enemy {

    private final int BOSS_BASE_ATTACK_DAMAGE = 10;//Low damage but cant run away from it. Need to use block
    private final int BOSS_SPECIAL_ATTACK_DAMAGE = 60; //High damage but easy to run away from.
    private final int BOSS_MAXHEALTH = 100; //TODO: Increase
    private final int BOSS_ATTACK_COOLDOWN = 3000;
    private AnimationsState specialAttackstatePt1;
    private AnimationsState specialAttackstatePt2;
    private final int APPLY_SPECIAL_DAMEAGE_COL = 7;
    private boolean specialFirstHalf = true; //Indicate which half of the special move to play,


    public Boss(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth, boolean enabled) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight,levelWidth,enabled);

        //////////////////////////// SET UP ANIMATION STATES ////////////////////////////////
//            this.attackState = new AnimationsState(48,40,220,42,7,0,0);
//            this.dieState = new AnimationsState(135,173,220,42,11,1,0);
//            this.runningState = new AnimationsState(135,173,220,42,3,2,0);
//            this.idleState = new AnimationsState(135,173,220,42,3,3,0);
//            this.gotHitState = new AnimationsState(135,173,220,42,7,4,0);
//            this.specialAttackstatePt1 = new AnimationsState(135,173,180,42,9,5,0);
//            this.specialAttackstatePt2 = new AnimationsState(63,63,180,42,11,6,0);

        this.attackState = new AnimationsState(16 * BOSS_SCALE,13 * BOSS_SCALE,73 * BOSS_SCALE,14 * BOSS_SCALE,7,0,0);
        this.dieState = new AnimationsState(45 * BOSS_SCALE,57 * BOSS_SCALE,73 * BOSS_SCALE,14 * BOSS_SCALE,11,1,0);
        this.runningState = new AnimationsState(45 * BOSS_SCALE,57 * BOSS_SCALE,73 * BOSS_SCALE,14 * BOSS_SCALE,3,2,0);
        this.idleState = new AnimationsState(45 * BOSS_SCALE,57 * BOSS_SCALE,73 * BOSS_SCALE,14 * BOSS_SCALE,3,3,0);
        this.gotHitState = new AnimationsState(45 * BOSS_SCALE,57 * BOSS_SCALE,73 * BOSS_SCALE,14 * BOSS_SCALE,7,4,0);
        this.specialAttackstatePt1 = new AnimationsState(45 * BOSS_SCALE,57 * BOSS_SCALE,73 * BOSS_SCALE,14 * BOSS_SCALE,9,5,0);
        this.specialAttackstatePt2 = new AnimationsState(21 * BOSS_SCALE,21 * BOSS_SCALE,60 * BOSS_SCALE,14 * BOSS_SCALE,11,6,0);
        this.attackCooldown = BOSS_ATTACK_COOLDOWN;
        this.APPLY_DAMAGE_COL = 4;
        this.EnemyMovementSpeed = 2;
        this.alertRadius = 420;

        //////////////////////////// SET UP HEALTH / DAMAGE //////////////////////////////
        this.attackDamage = BOSS_BASE_ATTACK_DAMAGE;
        this.currHealth = BOSS_MAXHEALTH;

        //Initialise image for first animation
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null);
    }

    @Override
    protected void updateAnimationState() {
       if (this.playSpecialAttackAnimation) {
           if (this.specialFirstHalf) {
               this.animationsState.copy(this.specialAttackstatePt1);
               if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                   this.specialFirstHalf = false;
               }
           } else {
               this.animationsState.copy(this.specialAttackstatePt2);
               if (this.currentAnimationCol == APPLY_SPECIAL_DAMEAGE_COL) {
                   Handler.attack(this); //Wait until part way through this animation before actually hitting
               }
               if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                   this.specialFirstHalf = true;
                   this.playSpecialAttackAnimation = false;
                   this.attackTimer = 0;
               }
           }
       } else {
           super.updateAnimationState();
       }
    }

    @Override
    protected boolean proximity(Level level) { //Circle proximity
        return (Math.sqrt((this.x - target.getX()) * (this.x - target.getX()) + (this.y - target.getY()) * (this.y - target.getY())) < this.alertRadius);
    }

    @Override
    boolean pickup(Item pickup) {
        return false;
    }

    protected int getAttackDamage() { //To vary the amount of damage dealt with better weapons
        if (this.playSpecialAttackAnimation) {
            return this.BOSS_SPECIAL_ATTACK_DAMAGE;
        }
        return this.BOSS_BASE_ATTACK_DAMAGE;
    }

    protected void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
            if (this.spriteDirection == 1) { //facing right
                graphicsContext.drawImage(this.jfxImage, this.x - (this.idleState.getLeftBorder() - this.animationsState.getLeftBorder()) - this.animationsState.getLeftBorder(),
                        this.y - (this.idleState.getTopBorder() - this.animationsState.getTopBorder()) - this.animationsState.getTopBorder(),
                        this.spriteWidth, this.spriteHeight);
            } else {
                graphicsContext.drawImage(this.jfxImage, this.x - this.idleState.getLeftBorder() + this.animationsState.getRightBorder() + this.spriteWidth- this.animationsState.getLeftBorder(),
                        y - this.idleState.getTopBorder(),
                        -this.spriteWidth, this.spriteHeight);
            }
//            this.renderBoundingBox(graphicsContext);
//            this.renderAttackBoundingBox(graphicsContext);
    }


    protected void renderBoundingBox(GraphicsContext graphicsContext) {
        graphicsContext.setFill(new Color(0.5, 0.5, 0.5, 0.5));

        if (this.spriteDirection == 1) {
            graphicsContext.fillRect(this.x - (this.idleState.getLeftBorder() - this.animationsState.getLeftBorder()),
                    this.y - (this.idleState.getTopBorder() - this.animationsState.getTopBorder()),
                    this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder(),
                    this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder());
        } else {
            graphicsContext.fillRect(this.x - (this.idleState.getLeftBorder() - this.animationsState.getRightBorder()),
                    this.y - (this.idleState.getTopBorder() - this.animationsState.getTopBorder()),
                    this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder(),
                    this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder());
        }
    }


    protected Rectangle getAttackBounds() {
        if (this.spriteDirection == 1) {
            return new Rectangle((int) (this.x),
                    (int) (this.y - 10),
                    (int) (this.spriteWidth - this.idleState.getLeftBorder() - this.idleState.getRightBorder() + 48),
                    (int) (this.spriteHeight - this.idleState.getTopBorder() - this.idleState.getBottomBorder()) + 20);
        } else {
            return new Rectangle((int) this.x - 48, (int) (this.y - 10),
                    (int) (this.spriteWidth - this.idleState.getLeftBorder() - this.idleState.getRightBorder() + 48),
                    (int) (this.spriteHeight - this.idleState.getTopBorder() - this.idleState.getBottomBorder()) + 20);
        }
    }

    protected void renderAttackBoundingBox(GraphicsContext graphicsContext) {
        graphicsContext.setFill(new Color(0.9, 0.2, 0.2, 0.5));

        if (this.spriteDirection == 1) {
            graphicsContext.fillRect(this.x,
                    this.y - 10,
                    this.spriteWidth - this.idleState.getLeftBorder() - this.idleState.getRightBorder() + 48,
                    this.spriteHeight - this.idleState.getTopBorder() - this.idleState.getBottomBorder() + 20);
        } else {
            graphicsContext.fillRect((int) this.x - 48, (int) (this.y - 10),
                    (int) (this.spriteWidth - this.idleState.getLeftBorder() - this.idleState.getRightBorder() + 48),
                    (int) (this.spriteHeight - this.idleState.getTopBorder() - this.idleState.getBottomBorder()) + 20);
        }
    }


    @Override
    public Rectangle getBounds() {
        if (this.playDieAnimation) {
            return new Rectangle(0,0,0,0);
        } else {
            if (this.spriteDirection == 1) {
                return new Rectangle((int)(this.x - (this.idleState.getLeftBorder() - this.animationsState.getLeftBorder())),
                        (int)(this.y - (this.idleState.getTopBorder() - this.animationsState.getTopBorder())),
                        (int)(this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder()),
                        (int)(this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()));
            } else {
            return new Rectangle((int)(this.x - (this.idleState.getLeftBorder() - this.animationsState.getRightBorder())),
                    (int)(this.y - (this.idleState.getTopBorder() - this.animationsState.getTopBorder())),
                    (int)(this.spriteWidth - this.animationsState.getLeftBorder() - this.animationsState.getRightBorder()),
                    (int)(this.spriteHeight - this.animationsState.getTopBorder() - this.animationsState.getBottomBorder()));
            }
        }
    }

    protected void tick(double cameraX, double cameraY, Level level) {
        if(this.playGotAttackedAnimation || this.playDieAnimation || this.playAttackAnimation || this.playSpecialAttackAnimation) {
            this.velocityX = 0;
            this.velocityY = 0;
        } else {
            super.tick(cameraX, cameraY, level);
        }
        if (this.getAttackBounds().intersects(target.getBounds())) {
            this.attack();
        }
    }

    @Override
    protected void attack() {
        if (super.canAttack() && Game.getNextRandomInt() < 50) { //50% chance of special move. Will need to decrease this.
            this.playAttackAnimation = false;
            this.playSpecialAttackAnimation = true;
        }
    }

    @Override
    protected void playDeathSound() {
        //SoundController.PlayAudio("bossDeath"); //The sound effect isn't actually implemented yet tho
    }
}

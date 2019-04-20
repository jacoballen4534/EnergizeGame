package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.Level.BOSS_SCALE;

public class Boss extends Enemy {

        private final int BOSS_BASE_ATTACK_DAMAGE = 10;//TODO: Increase
        private final int BOSS_MAXHEALTH = 100; //TODO: Increase
        private final int BOSS_ATTACK_COOLDOWN = 2000;
        private AnimationsState specialAttackstatePt1;
        private AnimationsState specialAttackstatePt2;
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
            this.specialAttackstatePt1 = new AnimationsState(45 * BOSS_SCALE,57 * BOSS_SCALE,60 * BOSS_SCALE,14 * BOSS_SCALE,9,5,0);
            this.specialAttackstatePt2 = new AnimationsState(21 * BOSS_SCALE,21 * BOSS_SCALE,60 * BOSS_SCALE,14 * BOSS_SCALE,11,6,0);
            this.attackCooldown = BOSS_ATTACK_COOLDOWN;
            this.APPLY_DAMEAGE_COL = 0;
            this.EnemyMovementSpeed = 2;

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

        protected void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
                if (this.spriteDirection == 1) { //facing right
                    graphicsContext.drawImage(this.jfxImage, this.x - this.animationsState.getLeftBorder(), this.y - this.animationsState.getTopBorder(), this.spriteWidth, this.spriteHeight);
                } else {
                    graphicsContext.drawImage(this.jfxImage, this.x + this.spriteWidth - this.animationsState.getLeftBorder(),
                            this.y - this.animationsState.getTopBorder(), -this.spriteWidth, this.spriteHeight);
                }
                this.renderBoundingBox(graphicsContext);

        this.renderAttackBoundingBox(graphicsContext);
        }

        protected void tick(double cameraX, double cameraY, Level level) {
            if(this.playGotAttackedAnimation || this.playDieAnimation || this.playAttackAnimation || this.playSpecialAttackAnimation) {
                this.velocityX = 0;
                this.velocityY = 0;
            } else {
                super.tick(cameraX, cameraY, level);
            }
            if (this.getAttackBounds().intersects(target.getBounds())) {
                //Check to play scecial attack or normal attack
                this.attack();
            }
        }

        @Override
        void isPlayerInSight() {

        }

        @Override
        void findPlayer() {

        }

        @Override
        protected void attack() {
            if (super.canAttack() && Game.getNextRandomInt() < 99) { //50% chance of special move. Will need to decrease this.
                this.playAttackAnimation = false;
                this.playSpecialAttackAnimation = true;
            }
        }


    }

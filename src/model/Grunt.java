package model;

import javafx.embed.swing.SwingFXUtils;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grunt extends Enemy {

    private final int GRUNT_BASE_ATTACK_DAMAGE = 10;
    private final int GRUNT_MAXHEALTH = 100;
    private final int GRUNT_ATTACK_COOLDOWN = 2000;

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
        this.EnemyMovementSpeed = 3;

        //////////////////////////// SET UP HEALTH / DAMAGE //////////////////////////////
        this.attackDamage = GRUNT_BASE_ATTACK_DAMAGE;
        this.currHealth = GRUNT_MAXHEALTH;
        this.APPLY_DAMEAGE_COL = 7; //To apply damage part way through the attack animation
        this.alertRadius = 500;

        //Initialise image for first animation
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null);
    }

    @Override
    protected void updateAnimationState() {
        super.updateAnimationState();
    }

    protected void tick(double cameraX, double cameraY, Level level) {
        if(this.playGotAttackedAnimation || this.playDieAnimation || this.playAttackAnimation) {
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
    void isPlayerInSight() {

    }

    @Override
    void findPlayer() {

    }

    @Override
    protected void attack() {
       super.canAttack();
    }

}

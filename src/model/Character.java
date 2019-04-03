package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class Character extends GameObject{
    private String name;
    protected int health;
//    protected Sprites[] sprites;
    protected Weapon weapon;
    protected float velocityX = 0, velocityY = 0;
    private String imgDirectory;
//    protected Direction direction;


    public Character(int x, int y){
        super(x,y);
    }

    abstract void attack();
    abstract void playSound();
    abstract void getHit();

    protected String getImgDirectory() {
        return imgDirectory;
    }

    protected String getName() {
        return name;
    }

    protected float getVelocityX() {
        return velocityX;
    }

    protected void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    protected float getVelocityY() {
        return velocityY;
    }

    protected void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    @Override
    public void tick() {
        this.x += this.velocityX;
        this.y += this.velocityY;
    }
}

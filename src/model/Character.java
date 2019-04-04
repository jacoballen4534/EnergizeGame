package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Character extends GameObject{
    private java.lang.String name;
    protected int health;
    protected Weapon weapon;
    protected float velocityX = 0, velocityY = 0;
//    protected Direction direction;


    public Character(int x, int y, boolean scale, BufferedImage image){
        super(x,y,scale,image);
    }

    abstract void attack();
    abstract void playSound();
    abstract void getHit();


    protected java.lang.String getName() {
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

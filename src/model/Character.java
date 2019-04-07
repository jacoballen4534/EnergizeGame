package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Character extends GameObject{
    private String name;
    protected int health;
    protected Weapon weapon;
    protected float velocityX = 0, velocityY = 0;

    public Character(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int rederHeight) {
        super(xLocation, yLocation, spriteSheet, spriteSheetWidth, spriteSheetHeight, renderWidth, rederHeight);
    }

    abstract void attack();
    abstract void playSound();
    abstract void getHit();


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
    public void tick() {//Update position
        this.x += this.velocityX;
        this.y += this.velocityY;
    }
}

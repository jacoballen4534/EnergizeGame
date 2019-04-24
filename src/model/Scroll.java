package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

public class Scroll extends Item{
    private int damage;
    private long freezeDuration;
    private long windDuration;

    public Scroll(String name, String description, int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        super(name, description,xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight);
        this.damage = 50;
        this.freezeDuration = 5000L; //Freeze enemy's for 5 seconds
        this.windDuration = 1500L; //Quickly blow enemy's away
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {

        if (this.inCameraBounds(cameraX,cameraY)) {
            graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
//            this.renderBoundingBox(graphicsContext);
        }

    }

    @Override
    public void useItem(Protagonist user) {
        if (this.name.equals("Fire Scroll")) {
            Handler.fireScrollAttack(this);
        } else if (this.name.equals("Ice Scroll")) {
            Handler.freezeEnemys(this);
        } else if (this.name.equals("Wind Scroll")) {
            Handler.blowEnemysAway(this);
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public long getFreezeDuration() {
        return this.freezeDuration;
    }

    public long getWindDuration() {
        return this.windDuration;
    }
}

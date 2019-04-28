package model;

import javafx.scene.canvas.GraphicsContext;
import sample.DifficultyController;
import sample.SoundController;

import java.awt.image.BufferedImage;

public class Scroll extends Item{
    private int damage;
    private long freezeDuration;
    private long windDuration;

    public Scroll(String name, String description, int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        super(name, description,xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight);
        this.damage = DifficultyController.FIRE_SCROLL_DAMAGE.value;
        this.freezeDuration = DifficultyController.ICE_SCROLL_DURATION.value;//5000L; //Freeze enemy's for 5 seconds
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
            SoundController.playSoundFX("fireScroll");
        } else if (this.name.equals("Ice Scroll")) {
            Handler.freezeEnemies(this);
            SoundController.playSoundFX("iceScroll");
        } else if (this.name.equals("Wind Scroll")) {
            Handler.blowEnemiesAway(this);
            SoundController.playSoundFX("windScroll");
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

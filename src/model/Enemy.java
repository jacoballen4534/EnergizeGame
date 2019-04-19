package model;

import javafx.scene.canvas.GraphicsContext;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Character{
    protected Protagonist target; //For path finding.
    private boolean enabled; //For controlling the AI

    public Enemy(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth,
                 boolean enabled) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
        this.enabled = enabled;
    }

    protected void updateTarget(Protagonist target) {
        this.target = target;
    }

    protected void tick(double cameraX, double cameraY, Level level) { //To provide a method to allow level to be passed in

    }

    protected void tick(double cameraX, double cameraY) {
        super.tick(cameraX,cameraY);
    }


    abstract void isPlayerInSight();
    abstract void findPlayer();

}

package model;

import javafx.scene.canvas.GraphicsContext;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Character{
    private Character target; //For path finding.

    public Enemy(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, Character target, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
        this.target = target;
    }

    @Override
    protected void updateTarget(Character target) {
        this.target = target;
    }

    abstract void isPlayerInSight();
    abstract void findPlayer();

}

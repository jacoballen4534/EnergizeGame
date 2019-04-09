package model;

import javafx.scene.canvas.GraphicsContext;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Character{
    private Character target; //For path finding.

    public Enemy(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, Character target) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight);
        this.target = target;
    }

    abstract void isPlayerInSight();
    abstract void findPlayer();

}

package model;

import javafx.scene.canvas.GraphicsContext;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Character{
    private Character target; //For path finding.

    public Enemy(int x, int y, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight, Character target) {
        super(x, y, image, spriteSheetWidth, spriteSheetHeight, renderWidth, renderHeight);
        this.target = target;
    }

    abstract void isPlayerInSight();
    abstract void findPlayer();

}

package model;

import javafx.scene.canvas.GraphicsContext;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Character{
    private Character target;

    public Enemy(int x, int y, boolean scale, BufferedImage image) {
        super(x, y, scale, image);
    }

    abstract void isPlayerInSight();
    abstract void findPlayer();

}

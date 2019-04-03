package model;

import javafx.scene.canvas.GraphicsContext;
import java.awt.*;

public abstract class Enemy extends Character{
    private Character target;

    public Enemy(int x, int y, boolean scale) {
        super(x, y, scale);
    }

    abstract void isPlayerInSight();
    abstract void findPlayer();

}

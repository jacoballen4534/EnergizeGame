package model;

import javafx.scene.canvas.GraphicsContext;
import java.awt.*;

public abstract class Enemy extends Character{
    private Character target;

    public Enemy(int x, int y) {
        super(x, y);
    }

    abstract void isPlayerInSight();
    abstract void findPlayer();

}

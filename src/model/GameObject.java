package model;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class GameObject {


    protected int x,y;
    protected boolean isSolid = false;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public abstract void tick();
    public abstract void render(GraphicsContext graphicsContext);
    public abstract Rectangle getBounds();

    /////////////////////////////////////////
    /*----------GETTERS AND SETTERS--------*/
    /////////////////////////////////////////
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

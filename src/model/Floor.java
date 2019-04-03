package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Floor extends Tile {

    public Floor(int x, int y, boolean scale) {
        super(x, y, scale);
        this.renderHeight = 0;

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.DODGERBLUE);
        graphicsContext.fillRect(this.x, this.y, this.width, this.height);
    }

    @Override
    public Rectangle getBounds() {
        //Size is zero so nothing intersects with it.
        return new Rectangle(0,0,0,0);
    }
}

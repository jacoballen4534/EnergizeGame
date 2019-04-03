package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Wall extends Tile {

    public Wall(int x, int y, boolean scale) {
        super(x , y, scale);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(this.x,this.y, this.width, this.height);

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public int getWidth() {
        return super.getWidth();
    }


}

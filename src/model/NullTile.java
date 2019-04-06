package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NullTile extends Tile {

    public NullTile(int xLocation, int yLocation, int renderWidth, int renderHeight, boolean solid) {
        super(xLocation, yLocation, null, 0, 0, renderWidth, renderHeight);
        this.solid = solid;
    }

    @Override
    public void render(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(this.x,this.y, this.spriteWidth, this.spriteHeight);
    }

    @Override
    protected void loadSpriteSheet(BufferedImage image) {

    }
}

package model;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Tile extends GameObject{
    protected boolean solid = false;

    public Tile(int x, int y, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight){
        super(x,y, image, spriteSheetWidth, spriteSheetHeight, renderWidth, renderHeight);
    }

    @Override
    public void tick() {

    }

    protected Rectangle getBounds() {
        if (this.solid) {
            return new Rectangle(this.x, this.y, this.spriteWidth, this.spriteHeight);
        } else {
            return new Rectangle(0, 0, 0, 0);
        }
    }
}

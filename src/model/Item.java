package model;

import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public abstract class Item extends GameObject{
    public Item(int xLocation, int yLocation, BufferedImage sprite, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight) {
        super(xLocation, yLocation, sprite, spriteWidth, spriteHeight, renderWidth, renderHeight);
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0),null);
        this.isSolid = false;
    }

    public abstract void useItem();
}

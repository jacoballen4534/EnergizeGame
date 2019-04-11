package model;

import java.awt.image.BufferedImage;

public abstract class Item extends GameObject{
    public Item(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight) {
        super(xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight, renderWidth, renderHeight);
    }
}

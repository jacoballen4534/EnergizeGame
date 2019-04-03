package model;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage spriteSheet;
    private int spriteWidth;
    private int spriteHeight;

    public String(BufferedImage spriteSheet, int width, int height) {
        this.spriteSheet = spriteSheet;
        this.spriteWidth = width;
        this.spriteHeight = height;
    }

    public BufferedImage getSprite (int x, int y, int width, int height) {
        return spriteSheet.getSubimage(x * spriteWidth,y * spriteHeight,width,height);
    }



}

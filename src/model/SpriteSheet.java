package model;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage spriteSheet;
    private int spriteWidth;
    private int spriteHeight;


    public SpriteSheet(BufferedImage spriteSheet, int width, int height) {
        this.spriteSheet = spriteSheet;
        this.spriteWidth = width;
        this.spriteHeight = height;
    }

    public SpriteSheet(BufferedImage spriteSheet, int width, int height, int leftBorder,
                       int rightBorder, int topBorder, int bottomBorder) {
        this.spriteSheet = spriteSheet;
        this.spriteWidth = width;
        this.spriteHeight = height;
    }

    public BufferedImage getSprite (int x, int y) {
        return spriteSheet.getSubimage(x * this.spriteWidth,y * this.spriteHeight,this.spriteWidth,this.spriteHeight);
    }



}

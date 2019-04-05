package model;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage spriteSheet;
    private int spriteWidth;
    private int spriteHeight;


    public SpriteSheet(BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        this.spriteSheet = spriteSheet;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    //Returns a sprite with border around it.
    public BufferedImage getSprite (int x, int y) {
        return spriteSheet.getSubimage(x * this.spriteWidth,y * this.spriteHeight,this.spriteWidth,this.spriteHeight);
    }

}

package model;

import java.awt.image.BufferedImage;

public abstract class Tile extends GameObject{

    public Tile(int x, int y, boolean scale, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight){
        super(x,y, scale, image, spriteSheetWidth, spriteSheetHeight);
        this.movable = false;
    }

}

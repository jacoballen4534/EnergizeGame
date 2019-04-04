package model;

import java.awt.image.BufferedImage;

public abstract class Tile extends GameObject{

    public Tile(int x, int y, boolean scale, BufferedImage image){
        super(x,y, scale, image);
        this.movable = false;
    }

}

package model;

public abstract class Tile extends GameObject{

    public Tile(int x, int y, boolean scale){
        super(x,y, scale);
        this.movable = false;
        this.renderHeight = 1;
    }

}

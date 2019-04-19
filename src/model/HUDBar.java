package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import sample.Game;

import java.awt.image.BufferedImage;

public class HUDBar extends GameObject{


    private int xIndent = 50;
    private int yIndent = 50;
    private int currVal;
    private int maxVal;
    private float valPercent;
    private Color outlineColour;
    private Color fillColour;
    private Polygon health2D;

    public HUDBar(int xLocation, int yLocation, BufferedImage spriteSheet, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight,
                  int currVal, int maxVal, Color fillColour, Color outlineColour) {
        super(xLocation, yLocation, spriteSheet, spriteWidth, spriteHeight, renderWidth, renderHeight);
        //this.spriteWidth = renderWidth;
        //this.spriteHeight = renderHeight;
        this.isSolid = false;
        this.currVal = currVal;
        this.maxVal = maxVal;
        this.valPercent = (float)currVal/maxVal;
        this.health2D = new Polygon();
        this.fillColour = fillColour;
        this.outlineColour = outlineColour;
        this.x = xLocation;
        this.y = yLocation;
    }

    public int getCurrVal() {
        return currVal;
    }

    public void setCurrVal(int currVal) {
        this.currVal = currVal;
    }

    public float getValPercent() {
        return valPercent;
    }

    public Polygon getBar2D() {
        return health2D;
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
        graphicsContext.setFill(outlineColour);
        graphicsContext.fillRect(cameraX+this.x,cameraY +this.y,
                this.spriteWidth,this.spriteHeight);
        graphicsContext.setFill(fillColour);
        graphicsContext.fillRect(cameraX+this.x+5,cameraY + this.y+5,
                (this.spriteWidth-10)*this.valPercent,this.spriteHeight-10);
    }

    @Override
    protected void tick(double cameraX, double cameraY) {
        super.tick(cameraX, cameraY);
        valPercent = (float)currVal/maxVal;
    }
}

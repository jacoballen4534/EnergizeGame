package model;

import com.sun.prism.Graphics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.Map;
import sample.Game;

public class HUD{

    private class Coordinate{
        public int x;
        public int y;
        public Coordinate(int x, int y){
            this.x=x;
            this.y=y;
        }
    }

    private int playerID;
    private HUDBar healthBar;
    private HUDBar energyBar;
    private int width, height;
    private Coordinate HUDCoord;
    //Relative coordinates to HUD box
    private Coordinate healthCoord = new Coordinate(20,20);
    private Coordinate energyCoord = new Coordinate(20,45);

    public HUD( int playerID, int maxHealth, int maxEnergy, int width, int height,
                int xIndent, int yIndent) {
        this.playerID = playerID;
        this.width = width;
        this.height = height;
        int topOfHUD = Game.SCREEN_HEIGHT-yIndent-(height*this.playerID);
        HUDCoord = new Coordinate(xIndent,topOfHUD);
        this.healthBar = new HUDBar(xIndent+healthCoord.x,
                topOfHUD + healthCoord.y,
                null,0,0,(int)(0.8*width),(int)(0.3*height),
                maxHealth,maxHealth,Color.RED,Color.BLACK);
        this.energyBar = new HUDBar(xIndent + energyCoord.x,
                topOfHUD + energyCoord.y,
                null,0,0,(int)(0.8*width),(int)(0.3*height),
                maxEnergy,maxEnergy,Color.MEDIUMTURQUOISE,Color.BLACK);

    }

    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY){

        //System.out.format("X: %s, Y: %s%n", cameraX, cameraY);
        //System.out.println(playerID);

        //Render backdrop
        graphicsContext.setFill(Color.DARKGREY);
        graphicsContext.fillRect(cameraX+HUDCoord.x,cameraY + HUDCoord.y,
                width,height);

        healthBar.render(graphicsContext,cameraX,cameraY);
        energyBar.render(graphicsContext,cameraX,cameraY);
    }

    public void setHealth(int health){
        healthBar.setCurrVal(health);
    }

    public int getEnergy(){
        return energyBar.getCurrVal();
    }

    public float getEnergyPercent(){
        return energyBar.getValPercent();
    }

    public void setEnergy(int energy){
        energyBar.setCurrVal(energy);
    }

    public void tick(double cameraX, double cameraY){
        healthBar.tick(cameraX, cameraY);
        energyBar.tick(cameraX, cameraY);
    }
}

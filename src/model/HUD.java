package model;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Game;

import java.util.Date;

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
    private int minutes = 0;
    private int seconds = 0;
    private int width, height;
    private int barWidth, barHeight;
    private Coordinate HUDCoord;
    //Relative coordinates to HUD box
    private Coordinate healthCoord = new Coordinate(20,10);
    private Coordinate energyCoord = new Coordinate(20,60);

    public HUD( int playerID, int maxHealth, int maxEnergy, int width, int height,
                int xIndent, int yIndent) {
        this.playerID = playerID;
        this.width = width;
        this.height = height;
        this.barWidth = (int)(0.8*this.width);
        this.barHeight = (int)(0.3*this.height);
        int topOfHUD = Game.SCREEN_HEIGHT-yIndent-height;
        HUDCoord = new Coordinate(xIndent,topOfHUD);
        this.healthBar = new HUDBar(xIndent+healthCoord.x,
                topOfHUD + healthCoord.y,
                null,0,0,barWidth,barHeight,
                maxHealth,maxHealth,Color.RED,Color.BLACK);
        this.energyBar = new HUDBar(xIndent + energyCoord.x,
                topOfHUD + energyCoord.y,
                null,0,0,barWidth,barHeight,
                maxEnergy,maxEnergy,Color.MEDIUMTURQUOISE,Color.BLACK);
    }

    public int getMinutes(){
        return this.minutes;
    }

    public int getSeconds() {
        return this.seconds;
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

    public void setEnergy(int energy){
        energyBar.setCurrVal(energy);
    }



    public int getEnergy(){
        return energyBar.getCurrVal();
    }

    public float getEnergyPercent(){
        return energyBar.getValPercent();
    }

    private void updateTime() {

    }

    public void tick(double cameraX, double cameraY){
        healthBar.tick(cameraX, cameraY);
        energyBar.tick(cameraX, cameraY);
    }
}

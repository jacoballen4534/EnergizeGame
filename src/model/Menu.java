package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.Game;

public class Menu {
    private int width, height;
    private int xPos, yPos;
    private boolean visible;

    public Menu(int width, int height, int xPos, int yPos) {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.visible = false;
    }

    public void show(){visible=true;}
    public void hide(){visible=false;}

    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY){
        if (visible){
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setFont(Font.font("Serif",100));
            graphicsContext.fillText("PAUSED",cameraX+ Game.SCREEN_WIDTH/2,
                    cameraY+Game.SCREEN_HEIGHT/2);
        }
    }

}
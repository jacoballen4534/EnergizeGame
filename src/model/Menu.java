package model;

import javafx.scene.canvas.GraphicsContext;

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
    private void render(GraphicsContext graphicsContext, double cameraX, double cameraY){
        if (visible)
            graphicsContext.fillText("This is a menu",100,100);
    }

}
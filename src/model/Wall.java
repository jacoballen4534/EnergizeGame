package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {

    public Wall(int x, int y, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight) {
        super(x , y, image, spriteSheetWidth, spriteSheetHeight, renderWidth,renderHeight);

//        this.animationMaxCol = 4;//TODO: Set this once we have a wall sprte

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
//        graphicsContext.setFill(Color.BLACK);
//        graphicsContext.fillRect(this.x,this.y, this.spriteWidth, this.spriteHeight);

        if (this.inCameraBounds(cameraX,cameraY)) {
            graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
            this.renderBoundingBox(graphicsContext);
        }

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public int getWidth() {
        return super.getWidth();
    }

}

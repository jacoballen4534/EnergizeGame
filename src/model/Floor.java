package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Floor {

    private int x,y;
    private static final int width = 32, height = 32; //size of sprite
    private Image jfxImage;

    public Floor(int x, int y, BufferedImage image) {
        this.x = x * width;
        this.y = y * width;
        SpriteSheet spriteSheet = new SpriteSheet(image, width, height);
//        Need to convert it to a JFXImage to be able to draw it to the canvas
        this.jfxImage = SwingFXUtils.toFXImage(spriteSheet.getSprite(11,0), null);
    }



    public void render(GraphicsContext graphicsContext) {
//        graphicsContext.setFill(Color.FIREBRICK);
//        graphicsContext.fillRect(this.x, this.y, this.width, this.height);

        graphicsContext.drawImage(this.jfxImage, this.x, this.y, width, height);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}

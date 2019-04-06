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

        this.animationMaxRow = 0;
        this.animationMaxCol = 4;

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(GraphicsContext graphicsContext) {
//        graphicsContext.setFill(Color.BLACK);
//        graphicsContext.fillRect(this.x,this.y, this.spriteWidth, this.spriteHeight);
        graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);

    }

    protected void updateSprite() {
        if (this.animationCol < this.animationMaxCol) {
            this.animationCol++;
        } else {
            this.animationCol = 0;
        }
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.animationCol,this.animationRow), null);

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public int getWidth() {
        return super.getWidth();
    }

    @Override
    protected void loadSpriteSheet(BufferedImage image) {
        this.spriteSheet = new SpriteSheet(image, this.spriteWidth, this.spriteHeight);
    }


}

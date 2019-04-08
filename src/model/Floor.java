package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Floor extends Tile{

    private Image jfxImage;

    public Floor(int x, int y, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight, int spriteSheetCol, int spriteSheetRow) {
        super(x, y, image, spriteSheetWidth, spriteSheetHeight, renderWidth, renderHeight);
//        this.jfxImage = SwingFXUtils.toFXImage(spriteSheet.getSprite(spriteSheetCol,spriteSheetRow), null);
    }


    @Override
    public void tick() {

    }

    @Override
    protected void loadSpriteSheet(BufferedImage image) {
//        super.loadSpriteSheet(image);
    }

    public void render(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.FIREBRICK);
        graphicsContext.fillRect(this.x, this.y, this.spriteWidth, this.spriteHeight);

//        graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
    }
}

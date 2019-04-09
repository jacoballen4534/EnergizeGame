package model;

import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public class AnimationWall extends Wall {
    public AnimationWall(int x, int y, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, int renderWidth, int renderHeight, int animationMaxRow, int animationMaxCol) {
        super(x, y, image, spriteSheetWidth, spriteSheetHeight, renderWidth, renderHeight);
        this.animationMaxRow = animationMaxRow;
        this.animationMaxCol = animationMaxCol;

    }


    protected void updateSprite() {
        if (this.animationCol < this.animationMaxCol) {
            this.animationCol++;
        } else {
            this.animationCol = 0;
        }
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.animationCol,this.animationRow), null);

    }

}

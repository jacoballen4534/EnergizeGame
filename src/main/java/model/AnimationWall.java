package model;

import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public class AnimationWall extends Wall {
    public AnimationWall(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int animationMaxCol, int animationRow, int resetCol) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, animationRow, resetCol);
        this.animationsState = new AnimationsState(0,0,0,0,animationMaxCol,animationRow,resetCol);
    }


    protected void updateSprite() { //TODO:Look at moving this to super
        this.currentAnimationCol = this.animationsState.updateAnimationSprite(this.currentAnimationCol);
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.currentAnimationCol,this.animationsState.getAnimationRow()), null);
    }

}

package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Door extends GameObject {
    private int nextLevel;
    private TileType doorType; //Holds the type of door, eg up, right, down, left
    private boolean playOpenDoorAnimation = false;
    private boolean open = false;
    private AnimationsState openState;

    public Door(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int nextLevel, TileType doorType) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight);
        //To load the appropriate next room
        this.nextLevel = nextLevel;
        //Set up animation sprites
        this.openState = new AnimationsState(0,0,0,0, 3,0,0);
        this.doorType = doorType;


        //Get a sub image from the full sprite sheet, then convert this to an FXImage so it can be drawn
        //Initialise image for first animation
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0, 0), null);
    }

    protected void updateSprite() {
        if (this.playOpenDoorAnimation) {
            this.currentAnimationCol = this.animationsState.updateAnimationSprite(this.currentAnimationCol);
            this.animationsState.copy(this.openState);
            if (this.animationsState.isLastFrame(this.currentAnimationCol)) {
                this.playOpenDoorAnimation = false;
                this.open = true;
            }
        }

        // Get the next sprite in the animation then convert it so it can be drawn.
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.currentAnimationCol,this.animationsState.getAnimationRow()), null);
    }

    public void openDoor() {
        if (!this.open && !this.playOpenDoorAnimation) { //Only open the door once
            this.currentAnimationCol = 0;
            this.playOpenDoorAnimation = true;
        }
    }

    public boolean isOpen () {
        return this.open;
    }

    @Override
    public void render(GraphicsContext graphicsContext, double cameraX, double cameraY) {
        if (this.inCameraBounds(cameraX,cameraY)) {
            graphicsContext.drawImage(this.jfxImage, this.x - this.animationsState.getLeftBorder(),
                    this.y - this.animationsState.getTopBorder(), this.spriteWidth, this.spriteHeight);
        }
    }

    public TileType getDoorType () {
        return this.doorType;
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public int getNextLevel() {
        return nextLevel;
    }
}

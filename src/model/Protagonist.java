package model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Protagonist extends Character {
    private static int nextID; //Unique id for all characters, this will be used for multiplayer
    protected int id;
    private int lives;
    private KeyInput keyInput;
    private GraphicsContext graphicsContext;
//    public Inventory inventory;


    public Protagonist(int x, int y, boolean scale, BufferedImage image, int spriteSheetWidth, int spriteSheetHeight, KeyInput keyInput) {
        super(x, y, scale, image, spriteSheetWidth, spriteSheetHeight);
        this.id = nextID++;
        this.keyInput = keyInput;
        //This is the redder width and height.
        this.spriteWidth = 100;
        this.spriteHeight = 74;

        this.animationMaxRow = 15;
        this.animationMaxCol = 6;

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null); //Initialise image for first animation
    }

    @Override
    protected void updateSprite() { //TODO: Setup fsm, probably use enum
        this.leftBorder = 11;
        this.rightBorder = 18;
        this.topBorder = 6;
        this.bottomBorder = 5;

//        super.updateSprite();
        if (this.animationCol < this.animationMaxCol) {
            this.animationCol++;
        } else {
            this.animationCol = 0;

            if (this.animationRow < this.animationMaxRow) {
                this.animationRow++;
            } else {
                animationRow = 0;
            }
        }

        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(this.animationCol,this.animationRow), null);
    }

    @Override
    void attack() {

    }

    @Override
    void playSound() {

    }

    @Override
    void getHit() {

    }


    public void tick() {
        if (this.keyInput.up) this.velocityY = -5;
        else if(!this.keyInput.down) this.velocityY = 0;

        if (this.keyInput.down) this.velocityY = 5;
        else if(!this.keyInput.up) this.velocityY = 0;

        if (this.keyInput.right) this.velocityX = 5;
        else if(!this.keyInput.left) this.velocityX = 0;

        if (this.keyInput.left) this.velocityX = -5;
        else if(!this.keyInput.right) this.velocityX = 0;

        super.tick();

        //Turn around if protagonist has collided with something
        if (Handler.checkCollision(this)) {
            this.x += this.velocityX * -1;
            this.y += this.velocityY * -1;
        }

    }

    @Override
    public void render(GraphicsContext graphicsContext) {
        //TODO: After fsm's are setup, check if this can be implamented in gameobject
//        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0), null);
        graphicsContext.drawImage(this.jfxImage, this.x, this.y, this.spriteWidth, this.spriteHeight);
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    protected void loadSpriteSheet(BufferedImage image) {
        this.spriteSheet = new SpriteSheet(image, this.spriteWidth, this.spriteHeight); //Pass in physical width and height of each sprite
    }

    protected void GetHit(){
        System.out.println("I got hit!");
    }

}


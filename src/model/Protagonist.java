package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.HashMap;

public class Protagonist extends Character {
    private static int nextID; //Unique id for all characters, this will be used for multiplayer
    protected int id;
    private int lives;
    private KeyInput keyInput;
//     public Inventory inventory;


    public Protagonist(int x, int y, boolean scale, KeyInput keyInput) {
        super(x, y, scale);
        this.id = nextID++;
        this.keyInput = keyInput;
        this.height = 48;
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
        graphicsContext.setFill(Color.DARKSLATEBLUE);
        graphicsContext.fillRect(this.x, this.y,this.width, this.height);

    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    protected void GetHit(){
        System.out.println("I got hit!");
    }

}


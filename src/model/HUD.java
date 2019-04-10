package model;

import com.sun.prism.Graphics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import java.awt.image.BufferedImage;

public class HUD {

    private HealthBar healthBar;

    public HUD(HealthBar healthBar) {
        this.healthBar = healthBar;
    }

    public void render(GraphicsContext graphicsContext){
        Rectangle health = healthBar.getHealth2D();
        //graphicsContext.getCanvas().getGraphicsContext2D().fillRect();
    }
}

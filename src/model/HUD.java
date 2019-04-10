package model;

import com.sun.prism.Graphics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.awt.image.BufferedImage;

public class HUD {

    private HealthBar healthBar;

    public HUD(HealthBar healthBar) {
        this.healthBar = healthBar;
    }

    public void render(GraphicsContext graphicsContext){
        Rectangle health = healthBar.getHealth2D();
        graphicsContext.fillRect(health.getX(),health.getY(),
                health.getX()+health.getWidth(),health.getY()+health.getHeight());
        //graphicsContext.getCanvas().getGraphicsContext2D().fillRect();
    }

}

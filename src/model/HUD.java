package model;

import com.sun.prism.Graphics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.Map;
import sample.Game;

import java.awt.image.BufferedImage;

public class HUD{

    private HealthBar healthBar = new HealthBar(100,100);
    private Map map;

    public HUD(Map map) {
        this.map = map;
    }

    public void render(GraphicsContext graphicsContext,Double cameraX, Double cameraY){
        float healthPercentage = healthBar.getHealthPercentage();
        graphicsContext.setFill(Color.RED);
        graphicsContext.fillRect(cameraX,cameraY + Game.SCREEN_HEIGHT - 100,
                healthPercentage*Game.SCREEN_WIDTH,50);
//        System.out.format("X: %s, Y: %s%n", cameraX, cameraY);
    }

}

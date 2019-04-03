package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game extends Canvas {

    private AnimationTimer animationTimer;
    private long previousTime = System.nanoTime();
    private KeyInput keyInput;
    private final double width = Main.getStage().getWidth();
    private final double height = Main.getStage().getHeight();
    private double delta = 0;
    private double ns = 1000000000 / 60.0;
    private int frames = 0;
    private BufferedImage level = null;//map will load and hold all of the levels
    private Camera camera;
    private Protagonist protagonist = null;
    private BufferImageLoader imageLoader;

    public Game() {
        super(Main.getStage().getWidth(),Main.getStage().getHeight());
        Stage stage = Main.getStage();
        stage.setTitle("Tutorial Room");
        Group root = new Group();
        root.getChildren().add(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        init();
        stage.show();
        this.keyInput = new KeyInput(scene);
        this.imageLoader = new BufferImageLoader();
        this.camera = new Camera(0,0);
        level = imageLoader.loadImage("GameMap.png");
        loadLevel(level);
    }


    public void stop() {
        this.animationTimer.stop();
    }

    public void start() {
        this.animationTimer.start();
    }



    private void init() {
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                delta += (now - previousTime) / ns;
                previousTime = now;
                while (delta >= 1) {
                    tick();
                    delta--;
                }
                render();
            }
        };
    }


    private void tick() {
        Handler.tick();
        if (this.protagonist != null) {
            this.camera.tick(this.protagonist, this.width, this.height);
        }

    }

    private void render() {
        GraphicsContext graphicsContext = this.getGraphicsContext2D();
        //First put the background
        graphicsContext.setFill(Color.FIREBRICK);
        graphicsContext.fillRect(0,0,this.width, this.height);

        graphicsContext.translate(-this.camera.getX(), -this.camera.getY());
        Handler.render(graphicsContext);
        graphicsContext.translate(this.camera.getX(), this.camera.getY());

        //Then objects on top
    }

    private void loadLevel(BufferedImage level) {
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                int pixel = level.getRGB(x,y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 0 && green == 0 && blue == 0) { //Black = Wall
                    Handler.addObject(new Wall(x,y, true));
                } else if (red == 0 && green == 0 && blue == 255) { //Blue = Protagonist
                    Protagonist tempProtagonist = new Protagonist(x,y, true, this.keyInput);
                    Handler.addObject(tempProtagonist);
                    Handler.addObject(new Floor(x,y,true)); //Add tile under characters
                    this.protagonist = tempProtagonist;
                } else if (red == 255 && green == 255 && blue == 255) { //White = Floor
                    Handler.addObject(new Floor(x,y,true));

                }
            }
        }
    }

}

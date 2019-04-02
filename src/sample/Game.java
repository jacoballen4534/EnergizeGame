package sample;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Handler;
import model.KeyInput;
import model.Protagonist;

import java.awt.event.KeyAdapter;
import java.security.Key;

public class Game extends Canvas {

    private AnimationTimer animationTimer;
    private long previousTime = System.nanoTime();
    private Handler handler;
    private KeyInput keyInput;
    private final double width = Main.getStage().getWidth();
    private final double height = Main.getStage().getHeight();
    private double delta = 0;
    private double ns = 1000000000 / 60.0;
    private int frames = 0;

    public Game() {
        super(Main.getStage().getWidth(),Main.getStage().getHeight());
        Stage stage = Main.getStage();
        stage.setTitle("Tutorial Room");
        Group root = new Group();
        root.getChildren().add(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        init();
        this.handler = new Handler();
        stage.show();
        this.keyInput = new KeyInput(scene);
        handler.addObject(new Protagonist(100,100, this.keyInput));


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
        this.handler.tick();

    }

    private void render() {
        GraphicsContext graphicsContext = this.getGraphicsContext2D();

        //First put the background
        graphicsContext.setFill(Color.RED);
        graphicsContext.fillRect(0,0,this.width, this.height);

        handler.render(graphicsContext);
        //Then objects on top
    }


}

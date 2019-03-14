package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.util.ArrayList;

public class BricknBallGame {

    Ball ball;
    Platform platform;
    ArrayList<Brick> bricks;
    private final int howManyBricks = 30;
    private final int numberOfCols = 10;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final Stage stage;
    private final Scene scene;
    private boolean start = false;
    private AnimationTimer animationTimer;
    private long lastTime;
    private PerspectiveCamera camera;


    public BricknBallGame(Group root, Stage stage, Scene scene, PerspectiveCamera camera) {
        this.stage = stage;
        this.scene = scene;
        this.camera = camera;
        this.platform = new Platform(stage.getWidth()/2 - ,stage.getHeight() - 30/*brick height*/);
        root.getChildren().add(platform);

        camera.getTransforms().add(new Translate(stage.getWidth()/2, stage.getHeight()/2, -100));


        this.ball = new Ball();
//        root.getChildren().add(ball);

        int currentRow = 0;
        bricks = new ArrayList<>();
        for (int i = 0; i < howManyBricks; i++) {
            Brick brick = new Brick( Brick.getSize().getX() * i / stage.getWidth(), currentRow*Brick.getSize().getY());
            bricks.add(brick);
//            root.getChildren().add(brick);
        }

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    platform.setXDirection(new Point2D(-1, 0));
                    break;
                case RIGHT:
                    platform.setXDirection(new Point2D(1, 0));
                    break;
                case SPACE:
                    start = true;
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    platform.setXDirection(new Point2D(0, 0));
                    break;
                case RIGHT:
                    platform.setXDirection(new Point2D(0, 0));
                    break;
            }
        });


        this.lastTime = 0;
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long timePassed = now - lastTime;
                lastTime = now;
                gameUpdate(timePassed);
                System.out.println((int)1000000000.0/timePassed + " Fps");
            }
        };
        this.animationTimer.start();

    }

    public void gameUpdate(long timePassed) {
        this.platform.platformUpdate(timePassed);
    }


}

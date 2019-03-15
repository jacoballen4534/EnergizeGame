package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class BricknBallGame {

    private Ball ball;
    private Platform platform;
    private ArrayList<Brick> bricks;
    private final int howManyBricks = 30;
    private final int numberOfCols = 10;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private  Stage stage;
    private  Scene scene;
    private boolean start = false;
    private AnimationTimer animationTimer;
    private long lastTime;
    private PerspectiveCamera camera;
    private Rotate rotateX,rotateY,rotateZ;
    private Translate translate;
    private ArrayList<Border> borders;
    private final int borderThickness = 20;
    private final int borderDepth = 30;
    private double windowWidth;
    private double windowHeight;
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;


    public BricknBallGame(Group root, Stage stage, Scene scene, PerspectiveCamera camera) {
        this.stage = stage;
        this.scene = scene;
        this.camera = camera;

        windowHeight = scene.getHeight();
        windowWidth = scene.getWidth();

        camera.getTransforms().addAll(
                rotateX = new Rotate(0, windowWidth/2, windowHeight/2,0, Rotate.X_AXIS),
                rotateY = new Rotate(0, windowWidth/2, windowHeight/2,0, Rotate.Y_AXIS),
                rotateZ = new Rotate(0, windowWidth/2, windowHeight/2,0, Rotate.Z_AXIS),
                translate = new Translate(windowWidth/2,windowHeight/2,-800)
        );
        camera.setFieldOfView(60.0);

        //Create a border around the game. Cant change the order of these, as they are used for
        borders = new ArrayList<>();
        borders.add(new Border(new Point2D(0, windowHeight/2), new Point3D(borderThickness, windowHeight, borderDepth), Border.WallLocation.Left)); //Left
        borders.add(new Border(new Point2D(windowWidth, windowHeight/2), new Point3D(borderThickness, windowHeight, borderDepth), Border.WallLocation.Right)); //Right
        borders.add(new Border(new Point2D(windowWidth/2, 0), new Point3D(windowWidth, borderThickness, borderDepth), Border.WallLocation.Top)); //Top
        borders.add(new Border(new Point2D(windowWidth/2, windowHeight), new Point3D(windowWidth, borderThickness, borderDepth), Border.WallLocation.Bottom)); //Bottom
        for (Border border : borders) {
            root.getChildren().add(border);
        }


        this.platform = new Platform(stage.getWidth()/2,windowHeight - (double)borderThickness/2 - Platform.getSize().getY()/2, Color.BLUE);
        root.getChildren().add(platform);

        this.ball = new Ball(new Point2D(windowWidth/2, windowHeight/2), 10);
        root.getChildren().add(ball);

        int currentRow = 0;
        bricks = new ArrayList<>();
        for (int i = 0; i < howManyBricks; i++) {
            Brick brick = new Brick( Brick.getSize().getX() * i / stage.getWidth(), currentRow*Brick.getSize().getY());
            bricks.add(brick);
//            root.getChildren().add(brick);
        }

        this.scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    this.platform.setXDirection(new Point2D(-1, 0));
                    break;
                case RIGHT:
                    this.platform.setXDirection(new Point2D(1, 0));
                    break;
                case SPACE:
                    this.start = true;
                    break;
//                    To move the camera around
                case A:
                    translate.setX(translate.getX() - 20);
                    break;
                case D:
                    translate.setX(translate.getX() + 20);
                    break;
                case W:
                    translate.setY(translate.getY() - 20);
                    break;
                case S:
                    translate.setY(translate.getY() + 20);
                    break;
                case I:
                    rotateX.setAngle(rotateX.getAngle() + 5);
                    break;
                case J:
                    rotateY.setAngle(rotateY.getAngle() + 5);
            }
        });



        this.scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    this.platform.setXDirection(new Point2D(0, 0));
                    break;
                case RIGHT:
                    this.platform.setXDirection(new Point2D(0, 0));
                    break;
            }
        });

        this.scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            camera.translateZProperty().set(camera.getTranslateZ() + delta);
        });


        this.scene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });


        this.scene.setOnMouseDragged(event -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            mouseDeltaX = mouseOldX - mousePosX;
            mouseDeltaY = mousePosY - mouseOldY;

            if(event.isPrimaryButtonDown()) {

                rotateY.setAngle(rotateY.getAngle() - mouseDeltaX * 0.1);
                rotateX.setAngle(rotateX.getAngle() - mouseDeltaY * 0.1);
//                translate.setY(translate.getY() - mouseDeltaY);
//                translate.setZ(translate.getZ() + mouseDeltaY);
//                translate.setX(translate.getZ() + mouseDeltaX);
//                translate.setZ(translate.getZ() + mouseDeltaX);
            }

        });

    }



    public void play() {
        this.lastTime = 0;
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long timePassed = now - lastTime;
                lastTime = now;
                gameUpdate(timePassed);
//                System.out.println((int)1000000000.0/timePassed + " Fps");
            }
        };
        this.animationTimer.start();
    }

    private void gameUpdate(long timePassed) {
        this.platform.Update(timePassed, borders);
    }

}

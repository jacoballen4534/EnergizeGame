package sample;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.util.Random;

public class Game extends Canvas {

    //For FPS / UPS counter
    private long time = 0;
    private int frames = 0;
    private int updates = 0;
    private KeyInput keyInput;

    //For game loop
    private AnimationTimer animationTimer;
    private long previousTime = System.nanoTime();
    private double delta = 0;
    private final double NS = 1000000000 / 60.0;
    private boolean isPaused = false;
    private boolean pauseButtonDown = false;

    //For handling UI
    private Group root;
    private AnchorPane anchorPane;
    private VBox pauseGameVBox;
    private Menu pauseMenu;

    private Camera camera;
    private Protagonist protagonist = null;
    private Map map;
    private Stage stage;
    public static final int SCALE = 1; //To scale the full game
    public static final int PIXEL_UPSCALE = 64 * Game.SCALE; //Place each tile, 1 tile width form the next.
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    private static Random random = new Random(1);//used for enemy movement and map generation.

    public Game() {
        //Setup the canvas
        super(Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);
        stage = Main.getStage();
        stage.setTitle("Tutorial Room");
        root = new Group();
        /*try{
            root = new FXMLLoader().load(Main.class.getResourceAsStream("/fxmls/game.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }*/
        root.getChildren().add(this);
        Scene scene = new Scene(root, SCREEN_WIDTH,SCREEN_HEIGHT, false);
        scene.getStylesheets().add(Main.class.getResource("/css/globalStyle.css").toExternalForm());

        //Setting up anchor pane
        anchorPane = new AnchorPane();
        anchorPane.setLayoutX(SCREEN_WIDTH/2-300);
        anchorPane.setLayoutY(SCREEN_HEIGHT/2-300);
        anchorPane.setStyle("-fx-background-color: #8a8a8a;");
        anchorPane.setPrefSize(300,300);

        //Setting up contents of menu
        Label pauseTitle = new Label("Pause Menu");
        pauseTitle.setStyle("-fx-font-size:50");
        Button resumeButton = new Button("Resume");
        resumeButton.setOnMouseClicked(mouseEvent -> unpause());
        Button quitButton = new Button("Quit");
        quitButton.setOnMouseClicked(mouseEvent -> System.exit(0));

        //Setting up VBox
        pauseGameVBox = new VBox(5);
        anchorPane.getChildren().add(pauseGameVBox);
        pauseGameVBox.setAlignment(Pos.CENTER);
        pauseGameVBox.getChildren().add(pauseTitle);
        pauseGameVBox.getChildren().add(resumeButton);
        pauseGameVBox.getChildren().add(quitButton);
        root.getChildren().add(anchorPane);
        anchorPane.setVisible(false);

        stage.setScene(scene);

        init(); //Setup game loop
        stage.show();
        this.keyInput = new KeyInput(scene); //Keyboard inputs
        this.camera = new Camera(0,0);
        this.map = new Map(this);
        this.map.loadLevel(0);
        this.pauseMenu = new Menu(100,100,100,100);
        Handler.setCamera(this.camera);
        Handler.setMap(this.map);
        Handler.timeline.setCycleCount(Animation.INDEFINITE);
        Handler.timeline.play();
    }

    public static int getNextRandomInt(int bounds) {
        return random.nextInt(bounds);
    }


    public void stop() {
        this.animationTimer.stop();
    }
    public void start() {
        this.animationTimer.start();
    }
    public KeyInput getKeyInput() {
        return this.keyInput;
    }

    private void init() {
        Stage stage1 = this.stage; //Need to make a local copy to use inside the handle method
        String stageName = stage1.getTitle(); //To keep the base name
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) { //This gets called 60 times per second
                time += now - previousTime;
                //Every second, display how many ticks have occurred and frames have been rendered.
                if (time >= 1000000000.0f) {
                    stage1.setTitle(stageName + " | " + frames + " FPS | " + updates + " UPS");
                    time = 0;
                    frames = 0;
                    updates = 0;
                }

                delta += (now - previousTime) / NS;//Update the actual time that has passed since the last update
                previousTime = now;
                while (delta >= 1) { //If delta is < 1 this means frames have been missed so dont update so many times.
                    // To stick to 60 updates per second despite different hardware.
                    tick(); //Advance all game logic a step
                    delta--;
                    updates++;
                }
                frames++;
                render(); //Draw everything to the screen. This is uncapped and varies based on the hardware.

            }
        };
    }


    private void tick() {
        if (keyInput.getKeyPress("pause") || keyInput.getKeyPress("quit")){
            pause();
            System.out.println("Toggle game pause");
        }
        if (!isPaused){
            Handler.tick(this.camera.getX(), this.camera.getY(),this.keyInput);
            if (this.protagonist != null) { //Make sure there is a protagonist to pan towards
                this.camera.tick(this.protagonist, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT,
                        this.map.getCurrentLevelWidth() * PIXEL_UPSCALE, this.map.getCurrentLevelHeight() * PIXEL_UPSCALE);
            }
        }
    }

    private void render() {

        GraphicsContext graphicsContext = this.getGraphicsContext2D();
        //Translate the to where the camera is looking for proper coordinates.
        graphicsContext.translate(-this.camera.getX(), -this.camera.getY());

        Handler.render(graphicsContext, this.camera.getX(), this.camera.getY());
        pauseMenu.render(graphicsContext, this.camera.getX(), this.camera.getY());

        //Translate back
        graphicsContext.translate(this.camera.getX(), this.camera.getY());

    }

    public void setProtagonist (Protagonist protagonist) {
        this.protagonist = protagonist;
    }

    public Protagonist getProtagonist () {
        return this.protagonist;
    }

    public Map getMap() {
        return this.map;
    }

    private void pause(){
        isPaused = true;
        Handler.timeline.pause();
        anchorPane.setVisible(true);
    }

    private void unpause(){
        isPaused = false;
        anchorPane.setVisible(false);
        Handler.timeline.play();
    }
}

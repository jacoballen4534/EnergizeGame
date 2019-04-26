package sample;

import FXMLControllers.MainMenuController;
import Multiplayer.Client;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    //For handling UI
    private Group root;
    private InGameMenuController inGameMenuController;

    private Scene gameScene;
    private Camera camera;
    private Protagonist protagonist = null;
    private Map map;
    private Stage stage;
    public static final int SCALE = 1; //To scale the full game
    public static final int PIXEL_UPSCALE = 64 * Game.SCALE; //Place each tile, 1 tile width form the next.
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    private static Random randomMovement;//used for enemy movement.
    private static long randomSeed;
    private String onlineCommands = "";

    private MainMenuController controller;

    public Game(MainMenuController menuController, long _randomSeed) {
        //Setup the canvas
        super(Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);
        this.controller = menuController;
        controller.setGameActive(true);
        Handler.clearForNewGame();
        this.stage = Main.getStage();
        this.stage.setTitle("Tutorial Room");
        this.root = new Group();
        this.root.getChildren().add(this);
        this.gameScene = new Scene(root, SCREEN_WIDTH,SCREEN_HEIGHT, false);
        this.gameScene.getStylesheets().add(Main.class.getResource("/css/globalStyle.css").toExternalForm());
        randomSeed = _randomSeed;
        Utilities.saveNewHighScore("TestAdd", 1513560);

        stage.setScene(this.gameScene);

        stage.show();
        this.keyInput = new KeyInput(this.gameScene); //Keyboard inputs
        this.camera = new Camera(Game.SCREEN_WIDTH/2,Game.SCREEN_HEIGHT/2);
        randomMovement = new Random(randomSeed);

        //////////////////// Make the map /////////////////////////////////////
        this.map = new Map(this, randomSeed);
        this.map.loadLevel();

        //////////////////Load Menu//////////////////////
        inGameMenuController = new InGameMenuController(protagonist.getInventory(),()->unpause(),
                exitToTitleScreenEvent-> {
                    Handler.disconnectFromServer();
                    stage.setScene(Main.getMainScene());
                });
        inGameMenuController.AddMenusToRoot(root);

        init(); //Setup game loop
        Handler.setCamera(this.camera);
        Handler.setMap(this.map);
        Handler.timeline.setCycleCount(Animation.INDEFINITE);
        Handler.timeline.play();
        Handler.setGame(this);
    }

    public void addClient(Client client) {
        this.protagonist.addClient(client);
    }

    public void setOnlineCommand(String command) {
        this.onlineCommands = command;
    }


    public void hidePauseMenu () {
        this.inGameMenuController.hidePauseMenu();
    }

    public static int getNextRandomInt() {
        return randomMovement.nextInt(100);
    }

    public void start(){
        this.animationTimer.start();
    }

    private void pause(){
        this.animationTimer.stop();
        Handler.timeline.pause();
    }

    public void unpause(){
        this.stage.setScene(this.gameScene);
        this.previousTime = System.nanoTime(); // Reset the previous to now, so it doesnt make up for all the ticks that where missed in pause state.
        this.animationTimer.start();
        Handler.timeline.play();
    }

    public KeyInput getKeyInput() {
        return this.keyInput;
    }

    private void init() {
        Stage stage1 = this.stage; //Need to make a local copy to use inside the handle method
//        String stageName = stage1.getTitle(); //To keep the base name
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) { //This gets called 60 times per second
                time += now - previousTime;
                //Every second, display how many ticks have occurred and frames have been rendered.
                if (time >= 1000000000.0f) {
                    String timeRemaining = protagonist.updateTimer();
                    stage1.setTitle("Time Played: " + timeRemaining + " | "+ frames + " FPS | " + updates + " UPS");
//                    stage1.setTitle(stageName + " | " + frames + " FPS | " + updates + " UPS | Time Played: " + timeRemaining);
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
        if (keyInput.getKeyPressDebounced("pause") || keyInput.getKeyPressDebounced("quit")){
            this.pause();
            inGameMenuController.showPauseMenu();
            System.out.println("Toggle game pause");
        }
        if (keyInput.getKeyPressDebounced("inventory")){
            this.pause();
            inGameMenuController.showInventoryMenu();
            System.out.println("Open inventory");
            System.out.println(this.protagonist.getInventory().getItemCount());
        }
        Handler.tick(this.camera.getX(), this.camera.getY(),this.keyInput, this.onlineCommands);
        if (this.protagonist != null) { //Make sure there is a protagonist to pan towards
            this.camera.tick(this.protagonist, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT,
                    this.map.getCurrentLevelWidth() * PIXEL_UPSCALE, this.map.getCurrentLevelHeight() * PIXEL_UPSCALE);
        }
    }

    private void render() {

        GraphicsContext graphicsContext = this.getGraphicsContext2D();
        //Translate the to where the camera is looking for proper coordinates.
        graphicsContext.translate(-this.camera.getX(), -this.camera.getY());

        Handler.render(graphicsContext, this.camera.getX(), this.camera.getY());

        //Translate back
        graphicsContext.translate(this.camera.getX(), this.camera.getY());

    }

    public void setProtagonist (Protagonist protagonist) {
        this.protagonist = protagonist;
        Handler.setProtagonist(protagonist);
    }

    public Protagonist getProtagonist () {
        return this.protagonist;
    }

    public void addPlayer(int id) {
    OnlinePlayer temp = new OnlinePlayer((int)this.protagonist.getX()/Game.PIXEL_UPSCALE, (int)this.protagonist.getY()/Game.PIXEL_UPSCALE, PreLoadedImages.protagonistSpriteSheet,
    Level.PROTAGONIST_SPRITE_WIDTH, Level.PROTAGONIST_SPRITE_HEIGHT, (int) (Level.PROTAGONIST_SPRITE_WIDTH * Game.SCALE * Level.PROTAGONIST_SPRITE_SCALE),
    (int) (Level.PROTAGONIST_SPRITE_HEIGHT * Game.SCALE * Level.PROTAGONIST_SPRITE_SCALE), this.map.getCurrentLevelWidth());
    temp.setId(id);
    temp.updateLevelNumber(this.map.getCurrentLevelNumber());
    temp.updateLevelWidth(this.map.getCurrentLevelWidth());
    Handler.addPlayer(temp);
    }

    public Map getMap() {
        return this.map;
    }

    public static long getRandomSeed() {
        return randomSeed;
    }

    public static void setRandomSeed(long seed) {
        randomSeed = seed;
    }
}

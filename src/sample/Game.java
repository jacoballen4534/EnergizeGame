package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import model.*;

import java.io.*;
import java.net.URLDecoder;

public class Game extends Canvas {

    private AnimationTimer animationTimer;
    private long previousTime = System.nanoTime();
    private long time = 0; //For fps counter
    private int frames = 0;//For fps counter
    private int updates = 0;//For ups counter
    private KeyInput keyInput;
    private final double screenWidth = Main.getStage().getWidth();
    private final double screenHeight = Main.getStage().getHeight();
    private double delta = 0;
    private final double NS = 1000000000 / 60.0;
    private Camera camera;
    private Protagonist protagonist = null;
    private Map map;
    private static final String sp = File.separator; //Used to read/write to file
    private Stage stage;
    public static final int SCALE = 1; //Scale from pixel to screen size
    public static final int PIXEL_UPSCALE = 32 * Game.SCALE;

    public Game() {
        super(Main.getStage().getWidth(),Main.getStage().getHeight());
        stage = Main.getStage();
        
        stage.setTitle("Tutorial Room");
        Group root = new Group();
        root.getChildren().add(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        init();
        stage.show();
        this.keyInput = new KeyInput(scene);
        this.camera = new Camera(0,0);
        this.map = new Map(this);
        this.map.loadLevel(0);
        Handler.timeline.setCycleCount(Animation.INDEFINITE);
        Handler.timeline.play();
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
        Stage stage1 = this.stage;
        String stageName = stage1.getTitle();
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                Tick/sec counter (Uncomment frames++
                time += now - previousTime;
                if (time >= 1000000000.0f) {
//                    System.out.println(frames);
                    stage1.setTitle(stageName + " | " + frames + " FPS | " + updates + " UPS");
                    time = 0;
                    frames = 0;
                    updates = 0;
                }

                delta += (now - previousTime) / NS;
                previousTime = now;
                while (delta >= 1) {
                    tick();
                    delta--;
                    updates++;
                }
                frames++;
                render();

            }
        };
    }


    private void tick() {
        Handler.tick();
        if (this.protagonist != null) {
            this.camera.tick(this.protagonist, this.screenWidth, this.screenHeight,
                    this.map.getCurrentLevelWidth() * PIXEL_UPSCALE, this.map.getCurrentLevelHeight() * PIXEL_UPSCALE);
        }

    }

    private void render() {

        GraphicsContext graphicsContext = this.getGraphicsContext2D();
        //Translate the to where the camera is looking
        graphicsContext.translate(-this.camera.getX(), -this.camera.getY());


        Handler.render(graphicsContext, this.camera.getX(), this.camera.getY(), this.screenWidth, this.screenHeight);

        //Translate back
        graphicsContext.translate(this.camera.getX(), this.camera.getY());

        //Then objects on top
    }

    public void setProtagonist (Protagonist protagonist) {
        this.protagonist = protagonist;
    }


    public Map getMap() {
        return this.map;
    }


    //This may not be needed anymore after changing to read methods
    private void saveDataToFile(String dataToWrite) {
        //Cant write to file that is inside jar, so find where the jar is, make a text file there, then save things like settings and high scores
        String jarPath = "";
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            System.out.println(jarPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String completePath = jarPath.substring(0, jarPath.lastIndexOf("/")) + sp + "File_Name";
        File f = new File(completePath);
        try {
            if (!f.exists() && !f.createNewFile()) {
                System.out.println("File doesnt exist and creating file with path: " + completePath + " failed. ");
            } else {
                System.out.println("Input data exists, or file with path " + completePath + " created successfully. ");
                System.out.println("Absolute Path: "  +f.getAbsolutePath());
                System.out.println("Path: " + f.getPath());
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
                out.writeObject(dataToWrite);
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String recoverInputFromFile() {
        System.out.println("Reading data...");
        String completePath = "";
        String jarPath = "";
        String readData = "";
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            System.out.println(jarPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        completePath = jarPath.substring(0, jarPath.lastIndexOf("/")) + sp + "File_Name";
        File f = new File(completePath);

        if (f.exists()) {
            System.out.println("File exists. ");
            System.out.println("Absolute Path: "  +f.getAbsolutePath());
            System.out.println("Path: " + f.getPath());
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                readData = (String)in.readObject();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File doesnt exist, or path " + completePath + " is wrong. ");
        }
        return readData;
    }


}

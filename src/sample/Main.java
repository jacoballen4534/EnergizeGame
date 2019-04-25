package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import model.Utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.*;
import java.lang.String;

import java.io.InputStream;

public class Main extends Application {
    private static Stage stage;
    private static Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Utilities.initializeFiles();

        Clip clip = SoundController.playMusic("titleBGM");
        clip.addLineListener(lineEvent -> {
            if (!clip.isRunning()) clip.start();
        });

        //Attempts to load a custom font
        Font.loadFont(Main.class.getResourceAsStream("/fonts/beon.otf"), 10);

        stage = primaryStage;
        Parent root = (Parent) new FXMLLoader().load(Main.class.getResourceAsStream("/fxmls/mainMenu.fxml"));
        primaryStage.setTitle("Main Menu");

        //Loads a global stylesheet
//        File styleSheet = new File("resources/css/globalStyle.css");
        String url = Main.class.getResource("/css/globalStyle.css").toExternalForm();

        mainScene = new Scene(root, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, false);
        mainScene.getStylesheets().add(url);

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    static Stage getStage() {
        return stage;
    }

    static Scene getMainScene() {return mainScene;}
}

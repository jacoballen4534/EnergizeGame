package sample;

import FXMLControllers.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import model.SaveGameMenu;
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
    private static AnchorPane root = new AnchorPane(); //Make empty anchor pane here to be able to set children later.

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        if (args.length > 0) {
            MainMenuController.serverAddressString = args[0];
        }
        if (args.length > 1) {
            MainMenuController.serverPort = Integer.parseInt(args[1]);
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Utilities.initializeFiles(false);

        SoundController.playMusic("titleBGM");


        //Attempts to load a custom font
        Font.loadFont(Main.class.getResourceAsStream("/fonts/beon.otf"), 10);

        stage = primaryStage;
        root.getChildren().setAll((AnchorPane) new FXMLLoader().load(Main.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
        primaryStage.setTitle("Main Menu");

        //Loads a global stylesheet
//        File styleSheet = new File("resources/css/globalStyle.css");
        String url = Main.class.getResource("/css/globalStyle.css").toExternalForm();

        mainScene = new Scene(root, Game.SCREEN_WIDTH + Game.HUD_WIDTH, Game.SCREEN_HEIGHT, false);
        mainScene.getStylesheets().add(url);

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static Stage getStage() {
        return stage;
    }

    public static Scene getMainScene() {return mainScene;}

    public static AnchorPane getRoot() {
        return root;
    }
}

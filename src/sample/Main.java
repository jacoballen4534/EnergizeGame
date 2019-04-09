package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.lang.String;

import java.io.InputStream;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class Main extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Play background music
        //String musicFile = "/music/mainMenuTheme.mp3";
        //Media media = new Media(this.getClass().getResource(musicFile).toURI().toString());
        /*Media media = new Media(
                new File("resources/music/mainMenuTheme.mp3").toURI().toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setAutoPlay(true);*/

        //Attempts to load a custom font
        Font.loadFont(Main.class.getResourceAsStream("/fonts/beon.otf"), 10);

        stage = primaryStage;

        Parent root = (Parent) new FXMLLoader().load(Main.class.getResourceAsStream("/fxmls/mainMenu.fxml"));
        primaryStage.setTitle("Main Menu");


        //Loads a global stylesheet
//        File styleSheet = new File("resources/css/globalStyle.css");
        String url = Main.class.getResource("/css/globalStyle.css").toExternalForm();

        Scene scene = new Scene(root, Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT, false);
        scene.getStylesheets().add(url);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    static Stage getStage() {
        return stage;
    }
}

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.File;

public class Main extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Attempts to load a custom font
        File beon = new File("resources/fonts/beon.otf");
        Font.loadFont(
                getClass().getResourceAsStream("file:///" + beon.getAbsolutePath().replace("\\","/")),
                10
        );

        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/mainMenu.fxml"));
        primaryStage.setTitle("Main Menu");

        //Loads a global stylesheet
        File styleSheet = new File("resources/css/globalStyle.css");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("file:///" + styleSheet.getAbsolutePath().replace("\\","/"));

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    static Stage getStage() {
        return stage;
    }
}

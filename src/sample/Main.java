package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

public class Main extends Application {
    private static Stage stage;

    public static void main(String[] args) {
//        java.net.URL url = ClassLoader.getSystemResource("/home/matteas/Pictures/lyoko_logo.png");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../fxmls/mainMenu.fxml"));
        primaryStage.setTitle("Main Menu");


        File f = new File("resources/css/Viper.css");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\","/"));

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    static Stage getStage() {
        return stage;
    }
}

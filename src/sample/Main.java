package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    private static Stage stage;

    public static void main(String[] args) {
//        java.net.URL url = ClassLoader.getSystemResource("/home/matteas/Pictures/lyoko_logo.png");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        Parent root = (Parent) new FXMLLoader().load(Main.class.getResourceAsStream("/fxmls/mainMenu.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    static Stage getStage() {
        return stage;
    }
}

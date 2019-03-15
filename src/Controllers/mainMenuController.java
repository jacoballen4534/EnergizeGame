package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import sample.BricknBallGame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class mainMenuController implements Initializable {

    @FXML private AnchorPane mainMenuPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML private void newGameButtonPressed() throws IOException {
        changeStageName("New Game");
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();

        Group root = new Group();
        Scene scene = new Scene(root, 1024, 768, true);

        PerspectiveCamera camera = new PerspectiveCamera(true);

        scene.setCamera(camera);
        stage.setScene(scene);

        camera.setNearClip(1.0);
        camera.setFarClip(3000.0);

        BricknBallGame game = new BricknBallGame(root, stage, scene, camera);
        game.play();



//        Put this button on stage to leave game and return to mainMenu screen
        /*public void newGameBackButtonPressed() throws IOException {
            changeStageName("Main Menu");
            mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/mainMenu.fxml")));
        }*/
    }


    @FXML private void loadGameButtonPressed() throws IOException {
        changeStageName("Load Game");
        mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/loadGame.fxml")));
    }

    @FXML private void highScoreButtonPressed() throws IOException {
        changeStageName("High Score");
        mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/highScore.fxml")));
    }

    @FXML private void optionsButtonPressed() throws IOException {
        changeStageName("Options");
        mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/options.fxml")));
    }


    @FXML private void creditsButtonPressed() throws IOException {
        changeStageName("Credits");
        mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/credits.fxml")));
    }

    @FXML private void quitButtonPressed() {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.close();
    }



    private void changeStageName(String newStageName) {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }
}






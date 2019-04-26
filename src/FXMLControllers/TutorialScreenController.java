package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.AnchorPane;
import sample.Game;


public class TutorialScreenController implements Initializable {

    @FXML
    public AnchorPane tutorialScreenPane;

    private static Game game;
    public static MainMenuController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void continueButtonPressed(){
        game = new Game(controller, System.currentTimeMillis());
        game.start();
        controller.setGame(game);
        try{backButtonPressed();}
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Continue button pressed");
    }

    public void backButtonPressed() throws IOException {
       tutorialScreenPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    public static void setController(MainMenuController menuController){
        controller = menuController;
    }
}

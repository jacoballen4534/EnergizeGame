package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import sample.Game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TutorialMechanicsController implements Initializable {

    @FXML public AnchorPane tutorialScreenMechanicsPane;

    private static Game game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML public void startGameButtonPressed(){
        game = new Game(TutorialControlsController.controller, System.currentTimeMillis());
        game.start();
        TutorialControlsController.controller.setGame(game);
        try{titleButtonPressed();}
        catch (Exception e){
            System.out.println("\033[0;31m" + e.getMessage());
        }
        //System.out.println("Start game");
    }

    public void titleButtonPressed() throws IOException {
        tutorialScreenMechanicsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    public void previousPageButtonPressed() throws IOException{
        tutorialScreenMechanicsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/tutorialScreenItems.fxml")));
    }
}

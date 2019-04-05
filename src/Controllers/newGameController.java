package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class newGameController implements Initializable {

    @FXML public AnchorPane newGamePane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void quickPlayButtonPressed() throws IOException {
        System.out.println("Starts a new quick play game");
        Game game = new Game();
        game.start();
    }

    public void customGameButtonPressed() throws IOException {
        System.out.println("Opens custom game options");
    }

    public void newGameBackButtonPressed() throws IOException {
        changeStageName("Main Menu");
        newGamePane.getChildren().setAll((AnchorPane) new FXMLLoader().load(mainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }


    private void changeStageName(String newStageName) {
        Stage stage = (Stage) newGamePane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

}

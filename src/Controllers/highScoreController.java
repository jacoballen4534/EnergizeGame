package Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class highScoreController implements Initializable {
    @FXML public AnchorPane highScorePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void /*some list type*/loadHighScores(String filepath){
        //Figure out how file i/o in Java
        //Call a json parser here to load the json file
        //return it as a yet-to-be-confirmed data type
    }

    /*public void highScoreButtonPressed() {
        System.out.println("Shows High Scores");
    }*/

    public void highScoreBackButtonPressed() throws IOException {
        changeStageName("Main Menu");
        highScorePane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/mainMenu.fxml")));
    }

    private void changeStageName(String newStageName) {
        Stage stage = (Stage) highScorePane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

}

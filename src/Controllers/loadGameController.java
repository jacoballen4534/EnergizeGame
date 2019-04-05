package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class loadGameController implements Initializable {

    @FXML
    public AnchorPane loadGamePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void selectSaveButtonPressed(){
        System.out.println("Open save selector");
    }

    public void loadGameBackButtonPressed() throws IOException {
        changeStageName("Main Menu");
        loadGamePane.getChildren().setAll((AnchorPane) new FXMLLoader().load(mainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }


    private void changeStageName(String newStageName) {
        Stage stage = (Stage) loadGamePane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

}

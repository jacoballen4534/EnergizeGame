package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {

    @FXML
    private AnchorPane endScreenPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML public void titleButtonPressed() throws IOException {
        ChangeStageName("Main Menu");
        endScreenPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(MainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    private void ChangeStageName(String newStageName) {
        Stage stage = (Stage) endScreenPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }
}

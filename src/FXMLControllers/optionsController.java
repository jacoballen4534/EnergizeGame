package FXMLControllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class optionsController implements Initializable {
    public AnchorPane optionsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void changeResolutionButtonPressed() {
        System.out.println("Resolution settings opens");
    }

    public void changeVolumeButtonPressed() {
        System.out.println("Volume settings opens");
    }

    public void editKeyBindsButtonPressed() {
        System.out.println("KeyBind settings opens");
    }

    public void selectDifficultyButtonPressed() {
        System.out.println("Difficulty settings opens");
    }

    public void optionsBackButtonPressed() throws IOException {
        changeStageName("Main Menu");
        optionsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(MainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    private void changeStageName(String newStageName) {
        Stage stage = (Stage) optionsPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }
}

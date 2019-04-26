package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TutorialMechanicsController implements Initializable {

    @FXML public AnchorPane tutorialScreenMechanicsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startGameButtonPressed(){
        System.out.println("Refactor note");
    }

    public void titleButtonPressed() throws IOException {
        tutorialScreenMechanicsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    public void previousPageButtonPressed() throws IOException{
        tutorialScreenMechanicsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/tutorialScreenControls.fxml")));
    }
}

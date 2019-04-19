package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class creditsController implements Initializable {

    @FXML private AnchorPane creditsPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void creditsBackButtonPressed() throws IOException {
        changeStageName("Main Menu");
        creditsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(mainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }


    private void changeStageName(String newStageName) {
        Stage stage = (Stage) creditsPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

    private void testButtonPressed(){
        System.out.println("MY NAME IS JEFF");
    }

}

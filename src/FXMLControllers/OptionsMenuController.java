package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class OptionsMenuController implements Initializable {

    @FXML private AnchorPane optionsPane;
    @FXML private ComboBox difficultySelect;
    @FXML private Slider masterSlider;
    @FXML private Slider sfxSlider;
    @FXML private Slider musicSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void changeVolumeButtonPressed() {
        System.out.println("Volume settings opens");
    }

    public void selectDifficultyButtonPressed() {
        System.out.println("Difficulty settings opens");
    }

    @FXML public void titleButtonPressed() throws IOException {
        changeStageName("Main Menu");
        optionsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(MainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    private void changeStageName(String newStageName) {
        Stage stage = (Stage) optionsPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

    @FXML
    private void updateMasterVolume(){
        System.out.println("Change master volume");
    }

    @FXML
    private void updateSFXVolume(){
        System.out.println("Change sound effects' volume");
    }

    @FXML
    private void updateMusicVolume(){
        System.out.println("Change music volume");
    }
}

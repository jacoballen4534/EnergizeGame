package FXMLControllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.DifficultyController;
import sample.SoundController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class OptionsScreenController implements Initializable {

    @FXML private AnchorPane optionsPane;
    @FXML private ComboBox difficultySelect;
    @FXML private Slider masterSlider;
    @FXML private Slider sfxSlider;
    @FXML private Slider musicSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBox();
        setupVolumeSliders();
    }

    @FXML private void setupComboBox(){
        difficultySelect.setVisibleRowCount(3);
        difficultySelect.getItems().add("EASY");
        difficultySelect.getItems().add("NORMAL");
        difficultySelect.getItems().add("HARD");
        difficultySelect.setValue(DifficultyController.difficulty);
        difficultySelect.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                DifficultyController.setDifficulty(difficultySelect.getValue().toString());
            }
        });
    }

    @FXML private void setupVolumeSliders(){
        masterSlider.setValue(SoundController.MASTER_GAIN_CONTROL*100);
        sfxSlider.setValue(SoundController.SOUNDEFFECTS_GAIN_CONTROL*100);
        musicSlider.setValue(SoundController.MUSIC_GAIN_CONTROL*100);
    }

    @FXML public void titleButtonPressed() throws IOException {
        ChangeStageName("Main Menu");
        optionsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(MainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    private void ChangeStageName(String newStageName) {
        Stage stage = (Stage) optionsPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

    @FXML
    private void updateMasterVolume(){
        SoundController.MASTER_GAIN_CONTROL = (float) masterSlider.getValue()/100.0f;
        SoundController.updateVolume();
        System.out.println("Change master volume");
    }

    @FXML
    private void updateSFXVolume(){
        SoundController.SOUNDEFFECTS_GAIN_CONTROL = (float) sfxSlider.getValue()/100.0f;
        System.out.println("Change sound effects' volume");
    }

    @FXML
    private void updateMusicVolume(){
        SoundController.MUSIC_GAIN_CONTROL = (float) musicSlider.getValue()/100.0f;
        SoundController.updateVolume();
        System.out.println("Change music volume");
    }
}

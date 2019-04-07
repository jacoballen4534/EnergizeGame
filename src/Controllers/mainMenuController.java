package Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;



public class mainMenuController implements Initializable {

    @FXML private AnchorPane mainMenuPane;
    private Label focussedLabel = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML private void NewGameClicked() throws IOException {
        changeStageName("New Game");
        focussedLabel = UpdateFocussedLabel(focussedLabel,"newGame");
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/newGame.fxml")));
    }

    @FXML private void LoadGameClicked() throws IOException {
        changeStageName("Load Game");
        focussedLabel = UpdateFocussedLabel(focussedLabel, "loadGame");
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/loadGame.fxml")));
    }

    @FXML private void HighScoreClicked() throws IOException {
        changeStageName("High Score");
        focussedLabel = UpdateFocussedLabel(focussedLabel,"highScores");
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/highScore.fxml")));
    }

    @FXML private void OptionsClicked() throws IOException {
        changeStageName("Options");
        focussedLabel = UpdateFocussedLabel(focussedLabel, "options");
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/options.fxml")));
    }


    @FXML private void CreditsClicked() throws IOException {
        changeStageName("Credits");
        focussedLabel = UpdateFocussedLabel(focussedLabel, "credits");
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/credits.fxml")));
    }

    @FXML private void QuitClicked() {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.close();
    }

    private void changeStageName(String newStageName) {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

    private Label getLabelByID(String id){
        ObservableList<Node> menuLabels = ((VBox)(mainMenuPane.getChildren().get(0))).getChildren();
        Iterator<Node> it = menuLabels.iterator();
        while (it.hasNext()) {
            Label label = (Label)it.next();
            if (label.getId().equals(id)){
                System.out.println("Found label: " + id);
                return label;
            }
        }
        return null;
    }

    private Label UpdateFocussedLabel(Label currFocussedLabel, String id){
        Label newLabel = getLabelByID(id);
        if (currFocussedLabel != null){
            currFocussedLabel.getStyleClass().setAll("menulabel");
        }
        if (currFocussedLabel != newLabel){
            currFocussedLabel = newLabel;
            currFocussedLabel.getStyleClass().setAll("menulabel-clicked");
        }

        return currFocussedLabel;
    }
}






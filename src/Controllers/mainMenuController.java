package Controllers;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class mainMenuController implements Initializable {

    @FXML private AnchorPane mainMenuPane;
    @FXML private VBox miniMenuVBox;
    private Label focussedLabel = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML private void NewGameClicked() throws IOException {
        //changeStageName("New Game");
        focussedLabel = UpdateFocussedLabel(focussedLabel,"newGame");
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/newGame.fxml")));
    }

    @FXML private void LoadGameClicked() throws IOException {
        //changeStageName("Load Game");
        focussedLabel = UpdateFocussedLabel(focussedLabel, "loadGame");
        //VBox vbox = (VBox) mainMenuPane.getChildren().get(0);
        //vbox.getChildren().remove(options);
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/loadGame.fxml")));
    }

    @FXML private void HighScoreClicked() throws IOException {
        //changeStageName("High Score");
        focussedLabel = UpdateFocussedLabel(focussedLabel,"highScores");
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/highScore.fxml")));
    }

    @FXML private void OptionsClicked() throws IOException {
        //changeStageName("Options");
        focussedLabel = UpdateFocussedLabel(focussedLabel, "options");
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/options.fxml")));
    }


    @FXML private void CreditsClicked() throws IOException {
        //changeStageName("Credits");
        focussedLabel = UpdateFocussedLabel(focussedLabel, "credits");
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/credits.fxml")));
    }

    @FXML private void QuitClicked() {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.close();
    }

    private void ChangeStageName(String newStageName) {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

    private Label getLabelByID(String id){
        ObservableList<Node> menuLabels = ((VBox)(mainMenuPane.getChildren().get(0))).getChildren();
        Iterator<Node> it = menuLabels.iterator();
        while (it.hasNext()) {
            Label label = (Label)it.next();
            if (label.getId().equals(id)){
                //System.out.println("Found label: " + id);
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
        else{
            currFocussedLabel = null;
        }

        System.out.println(currFocussedLabel);
        return currFocussedLabel;
    }

    //TODO refactor switch statement to be less hardcoded
    private void UpdateMenu(Label currFocussedLabel){
        if (currFocussedLabel == null){
            FadeOutCurrentMenu();
            return;
        }
        String lblId = currFocussedLabel.getId();
        switch(lblId){
            case "newGame":
                ShowNewGameMenu();
                break;
            case "loadGame":
                ShowLoadGameMenu();
                break;
            case "highScores":
                ShowHighScoresMenu();
                break;
            case "options":
                ShowOptionsMenu();
                break;
            case "credits":
                ShowCreditsMenu();
                break;
        }
    }

    private void ShowNewGameMenu(){
        FadeOutCurrentMenu();
        Label testLabel = new Label("Testing");
        Label otherLabel = new Label("HELLO");
        miniMenuVBox.getChildren().add(testLabel);
        miniMenuVBox.getChildren().add(otherLabel);
        FadeInMenu(miniMenuVBox);
    }

    private void ShowLoadGameMenu(){
        FadeOutCurrentMenu();
    }

    private void ShowHighScoresMenu(){
        FadeOutCurrentMenu();
    }

    private void ShowOptionsMenu(){
        FadeOutCurrentMenu();
    }

    private void ShowCreditsMenu(){
        FadeOutCurrentMenu();
    }

    private void FadeOutCurrentMenu(){
        ObservableList<Node> currItems = miniMenuVBox.getChildren();
        if (currItems.isEmpty()){return;}

        FadeTransition ft = new FadeTransition();
        ft.setNode(miniMenuVBox);
        ft.setDuration(new Duration(1000));
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setAutoReverse(false);
        ft.play();
        //System.out.println(miniMenuVBox.getChildren());
        ft.setOnFinished(e -> miniMenuVBox.getChildren().removeAll(currItems));
    }

    private void FadeInMenu(Node node){
        FadeTransition ft = new FadeTransition();
        ft.setDuration(new Duration(1000));
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setNode(node);
        ft.play();
    }
}






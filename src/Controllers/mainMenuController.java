package Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import sample.Game;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class mainMenuController implements Initializable {

    @FXML private AnchorPane mainMenuPane;
    @FXML private VBox mainMenuVBox;

    private Label focussedLabel = null;
    private EventHandler QuickPlayClicked = event -> {
        System.out.println("Starts a new quick play game");
        Game game = new Game();
        game.start();
    };
    private EventHandler CustomPlayClicked = event -> System.out.println("Starts custom game");

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
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/loadGame.fxml")));
    }

    @FXML private void HighScoreClicked() throws IOException {
        ChangeStageName("High Score");
        //focussedLabel = UpdateFocussedLabel(focussedLabel,"highScores");
        //UpdateMenu(focussedLabel);
        mainMenuPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/highScore.fxml")));
    }

    @FXML private void OptionsClicked() throws IOException {
        //changeStageName("Options");
        focussedLabel = UpdateFocussedLabel(focussedLabel, "options");
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/options.fxml")));
    }


    @FXML private void CreditsClicked() throws IOException {
        ChangeStageName("Credits");
        //focussedLabel = UpdateFocussedLabel(focussedLabel, "credits");
        //UpdateMenu(focussedLabel);
        mainMenuPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResource("/fxmls/credits.fxml")));
    }

    @FXML private void QuitClicked() {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.close();
    }

    private void ChangeStageName(String newStageName) {
        Stage stage = (Stage) mainMenuPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

    private Node getNodeByID(String id){
        ObservableList<Node> menuLabels = ((VBox)(mainMenuPane.getChildren().get(0))).getChildren();
        Iterator<Node> it = menuLabels.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getId().equals(id)){
                //System.out.println("Found node: " + id);
                return node;
            }
        }
        return null;
    }

    private Label UpdateFocussedLabel(Label currFocussedLabel, String id){
        Label newLabel = (Label)getNodeByID(id);
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

    private VBox CreateSubMenu(){
        VBox subMenu = new VBox();
        subMenu.getStyleClass().setAll("submenu");
        subMenu.setId("subMenuVBox");
        return subMenu;
    }
    private void CreateSubmenuLabel(VBox subMenu, String labelText, int position, EventHandler mouseOnClick){
        Label label = new Label(labelText);
        label.getStyleClass().setAll("submenu-label");
        if (mouseOnClick != null) label.setOnMouseClicked(mouseOnClick);
        subMenu.getChildren().add(position,label);
    }

    //TODO refactor switch statement to be less hardcoded
    private void UpdateMenu(Label currFocussedLabel){
        if (currFocussedLabel == null){
            //FadeOutCurrentMenu();
            HideCurrentSubMenu();
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
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateSubmenuLabel(subMenu,"Quick Play",0, QuickPlayClicked);
        CreateSubmenuLabel(subMenu,"Custom Play",1,CustomPlayClicked);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(1,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void ShowLoadGameMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateSubmenuLabel(subMenu,"Game Save 1",0,event -> {
            System.out.println("Loads the first game save");
        });
        CreateSubmenuLabel(subMenu,"Game Save 2",1,event -> {
            System.out.println("Loads second game save");
        });
        CreateSubmenuLabel(subMenu,"Game Save 3",2,event -> {
            System.out.println("Loads third game save");
        });
        //Add submenu to menu
        mainMenuVBox.getChildren().add(2,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void ShowHighScoresMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateSubmenuLabel(subMenu,"Do stuff",0,null);
        CreateSubmenuLabel(subMenu,"Other stuff",1,null);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(4,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void ShowOptionsMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateSubmenuLabel(subMenu,"Change Keybindings",0,null);
        CreateSubmenuLabel(subMenu,"Change volume",1,null);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(3,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void ShowCreditsMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateSubmenuLabel(subMenu,"Do stuff",0,null);
        CreateSubmenuLabel(subMenu,"Other stuff",1,null);
        CreateSubmenuLabel(subMenu,"Even more stuff",1,null);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(5,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void HideCurrentSubMenu(){
        VBox subMenu = (VBox)getNodeByID("subMenuVBox");
        mainMenuVBox.getChildren().remove(subMenu);
    }

    /* DEPRECATED */
    /*
    private void FadeOutCurrentMenu(){
        ObservableList<Node> currItems = miniMenuVBox.getChildren();
        if (currItems.isEmpty()){return;}

        FadeTransition ft = new FadeTransition();
        ft.setNode(miniMenuVBox);
        ft.setDuration(new Duration(10));
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setAutoReverse(false);
        ft.play();
        //System.out.println(miniMenuVBox.getChildren());
        ft.setOnFinished(e -> miniMenuVBox.getChildren().removeAll(currItems));
    }*/
    /*private void FadeInMenu(Node node){
        FadeTransition ft = new FadeTransition();
        ft.setDuration(new Duration(10));
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setNode(node);
        ft.play();
    }*/
}






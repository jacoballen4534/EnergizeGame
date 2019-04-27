package FXMLControllers;

import Multiplayer.Client;
import Multiplayer.Server;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.util.Pair;
import sample.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

import static model.Utilities.readFile;
import static sample.FXMLUtils.*;


public class MainMenuController implements Initializable {

    //Macros
    private final int newGamePos = 2;
    private final int loadGamePos = 3;
    private final int optionsPos = 4;
    private final int highscorePos = 5;
    private final int creditsPos = 6;

    private Pair<String, Long> saveOne;
    private Pair<String, Long> saveTwo;
    private Pair<String, Long> saveThree;


    @FXML private AnchorPane mainMenuPane;
    @FXML private VBox mainMenuVBox;
    @FXML private Label Resume;

    public static Game game;
    private static boolean isGameActive = false;

    private Label focussedLabel = null;

    //Hard coding events for dynamic buttons, might refactor out later
    /*private EventHandler QuickPlayClicked = event -> {
        focussedLabel = UpdateFocussedLabel(focussedLabel,focussedLabel.getId());
        UpdateMenu(focussedLabel);
        game = new Game(this, System.currentTimeMillis());
        game.start();
    };*/
    private EventHandler QuickPlayClicked = event -> {
        focussedLabel = UpdateFocussedLabel(focussedLabel,focussedLabel.getId());
        UpdateMenu(focussedLabel);
        try {
            TutorialControlsController.setController(this);
            mainMenuPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/tutorialScreenControls.fxml")));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    };

    //////////////////////// Multi Player buttons ////////////////////////////////
    /////////////////////////////// HOST ////////////////////////////////
    private static final int serverPort = 4000; //This can be any address
    public static String serverAddressString = "localhost";

    private EventHandler HostGameClicked = event -> {
        //Setup server
        Server server = new Server(serverAddressString, serverPort, game);
        server.start();


        Client client = new Client(serverAddressString, serverPort, game, this);
        client.connect();
    };
    ///////////////////////////////////////////////////////////////



    ////////////////////////////////// JOIN /////////////////////////////////////////
    private EventHandler JoinGameClicked = event -> {
        Client client1 = new Client(serverAddressString, serverPort, game, this);
        client1.connect();

    };
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("///////////////INITIALISE/////////////////");
//        System.out.println("Active game is: " + isGameActive);
        Resume.managedProperty().bind(Resume.visibleProperty());
        Resume.setVisible(isGameActive);
    }

    @FXML private void ResumeClicked() {
        try {
            game.hidePauseMenu(); //Consider removing for design reasons
            game.unpause();
        } catch (NullPointerException e) {
            System.out.println("Cant resume an online game.");
        }
    }

    @FXML private void NewGameClicked() throws IOException {
        //changeStageName("New Game");
        focussedLabel = UpdateFocussedLabel(focussedLabel,"newGame");
        UpdateMenu(focussedLabel);
        //mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/newGame.fxml")));
    }

    @FXML private void LoadGameClicked() throws IOException {
        //changeStageName("Load Game");
        this.updateSaveStates();
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

    private Label UpdateFocussedLabel(Label currFocussedLabel, String id){
        Label newLabel = (Label)getNodeByID(id,this.mainMenuVBox);
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
        int labelPos = 0;
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateMenuLabel(subMenu,"Quick Play","submenu-label",labelPos++, QuickPlayClicked);
        CreateMenuLabel(subMenu,"Host Game","submenu-label",labelPos++,HostGameClicked);
        CreateMenuLabel(subMenu,"Join Game","submenu-label",labelPos++,JoinGameClicked);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(newGamePos,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void ShowLoadGameMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateMenuLabel(subMenu,saveOne.getKey(),"submenu-label",0, event -> {
            if (saveOne.getValue() != null) {
                focussedLabel = UpdateFocussedLabel(focussedLabel,focussedLabel.getId());
                UpdateMenu(focussedLabel);
                game = new Game(this, saveOne.getValue());
                game.start();
            } else {
                System.out.println("No map saved here");
            }
        });
        CreateMenuLabel(subMenu,saveTwo.getKey(),"submenu-label",1, event -> {
            if (saveTwo.getValue() != null) {
                focussedLabel = UpdateFocussedLabel(focussedLabel,focussedLabel.getId());
                UpdateMenu(focussedLabel);
                game = new Game(this, saveTwo.getValue());
                game.start();
            } else {
                System.out.println("No map saved here");
            }
        });
        CreateMenuLabel(subMenu,saveThree.getKey(),"submenu-label",2, event -> {
            if (saveThree.getValue() != null) {
                focussedLabel = UpdateFocussedLabel(focussedLabel,focussedLabel.getId());
                UpdateMenu(focussedLabel);
                game = new Game(this, saveThree.getValue());
                game.start();
            } else {
                System.out.println("No map saved here");
            }
        });
        //Add submenu to menu
        mainMenuVBox.getChildren().add(loadGamePos,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void updateSaveStates() {
        ArrayList<ArrayList<Pair<String, String>>> seeds = readFile("mapSeeds.txt", true);
        for (ArrayList<Pair<String, String>> block : seeds) {
            if (block.get(0).getValue().equalsIgnoreCase("NaN")) {
                saveOne = new Pair<>(block.get(0).getKey(), null);
            } else {
                saveOne = new Pair<>(block.get(0).getKey(), Long.parseLong(block.get(0).getValue()));
            }
            if (block.get(1).getValue().equalsIgnoreCase("NaN")) {
                saveTwo = new Pair<>(block.get(1).getKey(), null);
            } else {
                saveTwo = new Pair<>(block.get(1).getKey(), Long.parseLong(block.get(1).getValue()));
            }
            if (block.get(2).getValue().equalsIgnoreCase("NaN")) {
                saveThree = new Pair<>(block.get(2).getKey(), null);
            } else {
                saveThree = new Pair<>(block.get(2).getKey(), Long.parseLong(block.get(2).getValue()));
            }
        }
    }

    private void ShowOptionsMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateMenuLabel(subMenu,"Change Keybindings","submenu-label",0,null);
        CreateMenuLabel(subMenu,"Change volume","submenu-label",1,null);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(optionsPos,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void ShowHighScoresMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateMenuLabel(subMenu,"Do stuff","submenu-label",0,null);
        CreateMenuLabel(subMenu,"Other stuff","submenu-label",1,null);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(highscorePos,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void ShowCreditsMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();
        //Create labels
        CreateMenuLabel(subMenu,"Do stuff","submenu-label",0,null);
        CreateMenuLabel(subMenu,"Other stuff","submenu-label",1,null);
        CreateMenuLabel(subMenu,"Even more stuff","submenu-label",1,null);
        //Add submenu to menu
        mainMenuVBox.getChildren().add(creditsPos,subMenu);
        //System.out.println(subMenu.getChildren());
    }

    private void HideCurrentSubMenu(){
        VBox subMenu = (VBox)getNodeByID("subMenuVBox",mainMenuVBox);
        mainMenuVBox.getChildren().remove(subMenu);
    }

    public void setGameActive(boolean gameActive){
         if (isGameActive == gameActive) return;
         Resume.setVisible(gameActive);
         isGameActive = gameActive;
    }

    public static void setGame(Game game) {
        MainMenuController.game = game;
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






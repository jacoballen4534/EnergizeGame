package FXMLControllers;

import Multiplayer.Client;
import Multiplayer.Server;
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
import sample.SoundController;

import java.io.IOException;
import java.net.*;
import java.util.*;

import static model.Utilities.readFile;
import static sample.FXMLUtils.*;


public class MainMenuController implements Initializable {

    //Macros
    private final int newGamePos = 2;
    private final int loadMapPos = 3;
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
            System.out.println("\033[0;31m" + e.getMessage());
        }
    };

    //////////////////////// Multi Player buttons ////////////////////////////////
    /////////////////////////////// HOST ////////////////////////////////
    public static int serverPort = 4200; //This can be any address
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
            SoundController.changeMusic("gameBGM");
        } catch (NullPointerException e) {
            System.out.println("Cant resume an online game.");
        }
        
    }

    @FXML private void NewGameClicked() throws IOException {
        focussedLabel = UpdateFocussedLabel(focussedLabel,"newGame");
        UpdateMenu(focussedLabel);
    }

    @FXML private void LoadMapClicked() throws IOException {
        this.updateSaveStates();
        focussedLabel = UpdateFocussedLabel(focussedLabel, "loadMap");
        UpdateMenu(focussedLabel);
    }

    @FXML private void HighScoreClicked() throws IOException {
        ChangeStageName("High Score");
        mainMenuPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/highScore.fxml")));
    }

    @FXML private void OptionsClicked() throws IOException {
        ChangeStageName("Options");
        mainMenuPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("/fxmls/options.fxml")));
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
            case "loadMap":
                ShowLoadMapMenu();
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

    private void ShowLoadMapMenu(){
        //Remove previous submenu
        HideCurrentSubMenu();
        //Create submenu
        VBox subMenu = CreateSubMenu();

        //Create labels
        CreateMenuLabel(subMenu,saveOne.getKey(),"submenu-label",0, event -> {
            loadMapFromSaveState(saveOne);
        });
        CreateMenuLabel(subMenu,saveTwo.getKey(),"submenu-label",1, event -> {
            loadMapFromSaveState(saveTwo);
        });
        CreateMenuLabel(subMenu,saveThree.getKey(),"submenu-label",2, event -> {
            loadMapFromSaveState(saveThree);
        });
        //Add submenu to menu
        mainMenuVBox.getChildren().add(loadMapPos,subMenu);
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

    private void loadMapFromSaveState(Pair<String, Long> saveMap){
        if (saveMap.getValue() != null) {
            focussedLabel = UpdateFocussedLabel(focussedLabel,focussedLabel.getId());
            UpdateMenu(focussedLabel);
            if (game != null) {
                game.pause();
            }
            game = new Game(this, saveMap.getValue());
            game.start();
        } else {
            System.out.println("No map saved here");
        }
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
}






package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    //#defines
    private static final int WIDTH = 600;
    private static final int Height = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{



        Label title = new Label("Belda");

        //Main menu buttons
        Button newGameButton = new Button("New Game");
        Button loadGameButton = new Button("Load Game");
        Button highScoreButton = new Button("High Scores");
        Button OptionsButton = new Button("Options");
        Button CreditsButton = new Button("Credits");
        Button QuitButton = new Button("Quit");

        //Main menu scene and layout
        VBox menuLayout = new VBox(20);
        menuLayout.getChildren().addAll(title, newGameButton, loadGameButton, highScoreButton, OptionsButton, CreditsButton, QuitButton);
        Scene menuScene = new Scene(menuLayout, WIDTH, Height);


        //New game buttons
        Button quickPlayButton = new Button("Quick Play");
        Button customPlayButton = new Button("Custom Play");
        Button newGameBackButton = new Button("Back to Main Menu");

        //New game scene and layout
        VBox newGameLayout = new VBox(20);
        newGameLayout.getChildren().addAll(quickPlayButton, customPlayButton, newGameBackButton);
        Scene newGameScene = new Scene(newGameLayout, WIDTH, Height);


        //Load game buttons
        Button loadGameOptionButton = new Button("Load Game");
        Button loadGameBackButton = new Button("Back to Main Menu"); //Might be able to reuse the same back button in all menu's

        //Load game scene and layout
        VBox loadGameLayout = new VBox(20);
        loadGameLayout.getChildren().addAll(loadGameOptionButton, loadGameBackButton);
        Scene loadGameScene = new Scene(loadGameLayout, WIDTH, Height);


        //High score buttons
        Button viewHighScoresButton = new Button("View High Scores");
        Button highScoresBackButton = new Button("Back to Main Menu");

        //High score scene and layout
        VBox highScoreLayout = new VBox(20);
        highScoreLayout.getChildren().addAll(viewHighScoresButton, highScoresBackButton);
        Scene highScoreScene = new Scene(highScoreLayout, WIDTH, Height);


        //Options buttons
        Button changeResolutionButton = new Button("Change Resolution");
        Button changeVolumeButton = new Button("Change Volume");
        Button editKeyBindsButton = new Button("Edit Key Binds");
        Button selectDifficultyButton = new Button("Select Difficulty");
        Button optionsBackButton = new Button("Back to Main Menu");

        //Options scene and layout
        VBox optionsLayout = new VBox(20);
        optionsLayout.getChildren().addAll(changeResolutionButton, changeVolumeButton, editKeyBindsButton, selectDifficultyButton, optionsBackButton);
        Scene optionsScene = new Scene(optionsLayout, WIDTH, Height);


        //Credits buttons
        Button creditsBackButton = new Button("Back to Main Menu");

        //Credits scene and layout
        VBox creditsLayout = new VBox(20);
        creditsLayout.getChildren().addAll(creditsBackButton);
        Scene creditsScene = new Scene(creditsLayout, WIDTH, Height);


        //Quit buttons = none, maybe confirmation?



        //Action for all buttons

        //Main menu buttons
        newGameButton.setOnAction(event -> primaryStage.setScene(newGameScene));
        loadGameButton.setOnAction(event -> primaryStage.setScene(loadGameScene));
        highScoreButton.setOnAction(event -> primaryStage.setScene(highScoreScene));
        OptionsButton.setOnAction(event -> primaryStage.setScene(optionsScene));
        CreditsButton.setOnAction(event -> primaryStage.setScene(creditsScene));
        QuitButton.setOnAction(event -> primaryStage.close());

        //New game buttons
        quickPlayButton.setOnAction(event -> System.out.println("Starts a new quick play game"));
        customPlayButton.setOnAction(event -> System.out.println("Opens custom game options"));
        newGameBackButton.setOnAction(event -> primaryStage.setScene(menuScene));

        //load Game Buttons
        loadGameOptionButton.setOnAction(event -> System.out.println("Opens load game options"));
        loadGameBackButton.setOnAction(event -> primaryStage.setScene(menuScene));

        //High score buttons
        viewHighScoresButton.setOnAction(event -> System.out.println("Opens high scores"));
        highScoresBackButton.setOnAction(event -> primaryStage.setScene(menuScene));

        //Options buttons
        changeResolutionButton.setOnAction(event -> System.out.println("Opens resolution options"));
        changeVolumeButton.setOnAction(event -> System.out.println("Opens volume options"));
        editKeyBindsButton.setOnAction(event -> System.out.println("Opens key bind options"));
        selectDifficultyButton.setOnAction(event -> System.out.println("Opens difficulty options"));
        optionsBackButton.setOnAction(event -> primaryStage.setScene(menuScene));

        //Credits buttons
        creditsBackButton.setOnAction(event -> primaryStage.setScene(menuScene));


        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Game title");
        primaryStage.show();



    }
}

package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {

    private static final int ITEM_MULTIPLIER = 100;
    private static final int ENEMY_MULTIPLIER = 200;
    private static final int TIME_MULTIPLIER = 1;
    private static final int VICTORY_BONUS = 700;

    public static boolean gameVictory;
    private static int itemScore;
    private static int enemyScore;
    private static int timeScore;
    private static int victoryScore;
    private static int finalScore;

    @FXML
    private AnchorPane endScreenPane;

    @FXML private Label itemsCollectedLabel;
    @FXML private Label enemiesKilledLabel;
    @FXML private Label timeTakenLabel;
    @FXML private Label victoryBonusLabel;
    @FXML private Label finalScoreLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemsCollectedLabel.setText(Integer.toString(itemScore));
        enemiesKilledLabel.setText(Integer.toString(enemyScore));
        timeTakenLabel.setText(Integer.toString(timeScore));
        victoryBonusLabel.setText(Integer.toString(victoryScore));
        finalScoreLabel.setText(Integer.toString(finalScore));
    }

    @FXML public void titleButtonPressed() throws IOException {
        ChangeStageName("Main Menu");
        endScreenPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(MainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    private void ChangeStageName(String newStageName) {
        Stage stage = (Stage) endScreenPane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

    public static void SetScore(boolean victory, int numItems, int numEnemies, int time){
        gameVictory = victory;
        itemScore = numItems * ITEM_MULTIPLIER;
        enemyScore = numEnemies * ENEMY_MULTIPLIER;
        timeScore = time * TIME_MULTIPLIER; //Time in seconds
        victoryScore = (victory) ? VICTORY_BONUS:0;
        finalScore = itemScore + enemyScore + timeScore + victoryScore;
    }
}

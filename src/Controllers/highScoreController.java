package Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;


public class highScoreController implements Initializable {
    @FXML
    public AnchorPane highScorePane;
    private Label Person1;
    private Label Score1;
    private Label Person2;
    private Label Score2;
    private Label Person3;
    private Label Score3;
    private Label Person4;
    private Label Score4;
    private Label Person5;
    private Label Score5;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            File file = new File(highScoreController.class.getClassLoader().getResource("highScores.txt").getPath());
            Scanner scanner = new Scanner(file);
            ArrayList<Pair<String, Integer>> scores = new ArrayList<>();

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("//")){ //Ignore comments
                    String[] tokens = line.split("-");
                    Pair<String, Integer> score = new Pair<>(tokens[0], Integer.parseInt(tokens[1]));
                    scores.add(score);
                }
            }

            scores.forEach((n) -> System.out.println(n));

            Person1.setText("Hi");
            Score1.setText(scores.get(0).getKey());
            Score2.setText(scores.get(0).getValue().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }





    public void /*some list type*/loadHighScores(String filepath){
        //Figure out how file i/o in Java
        //Call a json parser here to load the json file
        //return it as a yet-to-be-confirmed data type
    }

    /*public void highScoreButtonPressed() {
        System.out.println("Shows High Scores");
    }*/

    public void highScoreBackButtonPressed() throws IOException {
        changeStageName("Main Menu");
        highScorePane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("../fxmls/mainMenu.fxml")));
    }

    private void changeStageName(String newStageName) {
        Stage stage = (Stage) highScorePane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

}

package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class highScoreController implements Initializable {
    @FXML
    public AnchorPane highScorePane;
    @FXML
    private Label Person1, Person2, Person3, Person4, Person5, Score1, Score2, Score3, Score4, Score5;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ArrayList<ArrayList<Pair<String,String>>> highScores = readFile("/highScores.txt");
            List<Pair<String, Integer>> scores = new ArrayList<>();

            //Put each score into list of pairs in the form of <Name,Score>
            for (ArrayList<Pair<String,String>> block : highScores) {
                scores.add(new Pair<>(block.get(0).getValue(), Integer.parseInt(block.get(1).getValue())));
            }

            //Sort the scores into descending order
            scores.sort((score1, score2) -> (score2.getValue() - score1.getValue()));

            Person1.setText(scores.get(0).getKey());
            Person2.setText(scores.get(1).getKey());
            Person3.setText(scores.get(2).getKey());
            Person4.setText(scores.get(3).getKey());
            Person5.setText(scores.get(4).getKey());

            Score1.setText(scores.get(0).getValue().toString());
            Score2.setText(scores.get(1).getValue().toString());
            Score3.setText(scores.get(2).getValue().toString());
            Score4.setText(scores.get(3).getValue().toString());
            Score5.setText(scores.get(4).getValue().toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //Array of arrays to make it more generic for better reusability.
    private ArrayList<ArrayList<Pair<String, String>>> readFile(String filepath) throws FileNotFoundException {
        ArrayList<ArrayList<Pair<String, String>>> allContent = new ArrayList<>();
        boolean openedBlock = false;
        InputStream stream = highScoreController.class.getResourceAsStream(filepath);
//        File file = new File(highScoreController.class.getClassLoader().getResource(filepath).getPath());
        Scanner scanner = new Scanner(stream);
        ArrayList<Pair<String, String>> blockOfData = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.startsWith("//")) { //Ignore comments
                if (!openedBlock) {
                    if (line.equalsIgnoreCase("<begin>")) {//Start new structure
                        blockOfData = new ArrayList<>();
                        openedBlock = true;
                    }
                    continue;
                } else { //Inside a begin statment
                    if (blockOfData != null && line.equalsIgnoreCase("<end>")) {
                        //Inset line items into
                        allContent.add(blockOfData);
                        openedBlock = false;
                        blockOfData = null;
                        continue;
                    } else {
                        // Split and add to lineItem
                        String[] tokens = line.split(":");
                        blockOfData.add(new Pair<>(
                                tokens[0].replaceAll("[^A-Za-z0-9]+", "").toLowerCase(),
                                tokens[1].replaceAll("[^A-Za-z0-9]+", "")));
                    }
                }
            }
        }
        return allContent;
    }


    public void highScoreBackButtonPressed() throws IOException {
        changeStageName("Main Menu");
        highScorePane.getChildren().setAll((AnchorPane) new FXMLLoader().load(mainMenuController.class.getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    private void changeStageName(String newStageName) {
        Stage stage = (Stage) highScorePane.getScene().getWindow();
        stage.setTitle(newStageName);
    }

}

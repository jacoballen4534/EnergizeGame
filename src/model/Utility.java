package model;

import Controllers.highScoreController;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Utility {
    //Array of arrays to make it more generic for better reusability.
    private ArrayList<ArrayList<Pair<String, String>>> readFile(String filepath) throws FileNotFoundException {
        ArrayList<ArrayList<Pair<String, String>>> allContent = new ArrayList<>();
        boolean openedBlock = false;
        File file = new File(highScoreController.class.getClassLoader().getResource(filepath).getPath());
        Scanner scanner = new Scanner(file);
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
}

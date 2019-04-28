package model;

import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Utilities {
    private static boolean openedBlock = false;
    private static ArrayList<Pair<String, String>> blockOfData = null;
    private static ArrayList<ArrayList<Pair<String, String>>> allContent = new ArrayList<>();


    //Array of arrays to make it more generic for better reusability.
    public static ArrayList<ArrayList<Pair<String, String>>> readFileInsideJar(String filepath, boolean keepFormat) {
        InputStream stream = Utilities.class.getResourceAsStream(filepath);
        Scanner scanner = new Scanner(stream);

        openedBlock = false;
        blockOfData = null;
        allContent = new ArrayList<>();

        while (scanner.hasNextLine()) {
            processLine(scanner.nextLine(), keepFormat);
        }
        return allContent;
    }



    private static void processLine(String line, boolean keepFormat) {
        try {
            if (!line.startsWith("//")) { //Ignore comments
                if (!openedBlock) {
                    if (line.equalsIgnoreCase("<begin>")) {//start new structure
                        blockOfData = new ArrayList<>();
                        openedBlock = true;
                    }
                } else { //Inside a begin statment
                    if (blockOfData != null && line.equalsIgnoreCase("<end>")) {
                        //Inset line items into
                        allContent.add(blockOfData);
                        openedBlock = false;
                        blockOfData = null;
                    } else {
                        // Split and add to lineItem
                        String[] tokens = line.split(":");
                        if (keepFormat) {
                            blockOfData.add(new Pair<>(tokens[0], tokens[1]));
                        } else {
                            blockOfData.add(new Pair<>(
                                    tokens[0].replaceAll("[^A-Za-z0-9_ ]+", "").toLowerCase(),
                                    tokens[1].replaceAll("[^A-Za-z0-9_ ]+", "")));
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            initializeFiles(true);
        }
    }



    public static ArrayList<ArrayList<Pair<String, String>>> readFile(String filepath, boolean keepFormat) {
        BufferedReader bufferedReader = null;
        String line = null;
        String fullFilePath = System.getProperty("user.home") + File.separator + "Group34GameSettings" + File.separator + filepath;

        try {
            bufferedReader = new BufferedReader(new FileReader(fullFilePath));
            line = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("\033[0;31m" + e.getMessage());
        }

        openedBlock = false;
        blockOfData = null;
        allContent = new ArrayList<>();

        while (line != null) {
            processLine(line, keepFormat);
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println("\033[0;31m" + e.getMessage());
            }
        }
        return allContent;
    }

    public static void saveNewHighScore(String name, int score) {
        //Need to read the old scores, to write them back after adding
        String toWrite = "";
        try {
            ArrayList<ArrayList<Pair<String, String>>> highScores = readFile("highScores.txt", true);
            //Sort the scores into descending order to check if the new score is greater than the lowest score
            highScores.sort((score1, score2) -> (Integer.parseInt(score2.get(1).getValue()) - Integer.parseInt(score1.get(1).getValue())));
            if (score > Integer.parseInt(highScores.get(highScores.size()-1).get(1).getValue())) { //Only replace if its a better score
                highScores.set(4, new ArrayList<>(Arrays.asList(new Pair<>("name", name), new Pair<>("score", Integer.toString(score)))));
                //Sort it again to put the new score in the correct position
                highScores.sort((score1, score2) -> (Integer.parseInt(score2.get(1).getValue()) - Integer.parseInt(score1.get(1).getValue())));
            }

            toWrite = prepareBlockToWrite(highScores);

            String path = System.getProperty("user.home") + File.separator + "Group34GameSettings";
            PrintWriter writer = new PrintWriter(new FileWriter(path + File.separator + "highScores.txt", false));
            writer.printf("%s", toWrite);
            writer.close();

        } catch (IOException e) {
            System.out.println("\033[0;31m" + e.getMessage());
        }
    }


    public static void initializeFiles(boolean override) {
        try {
            String path = System.getProperty("user.home") + File.separator + "Group34GameSettings";
            File rootDir = new File(path);


            if (rootDir.exists() || rootDir.mkdirs()) {
                System.out.println("Directory already exists or was created");

                File highScorePath = new File(path + File.separator + "highScores.txt");
                if (override || !highScorePath.exists()) { //If the highScore file doesnt exist. Make and initialize it.
                    PrintWriter writer = new PrintWriter(new FileWriter(path + File.separator + "highScores.txt", false));
                    writer.printf("%s", prepareBlockToWrite(readFileInsideJar("/highScores.txt", true))); //Write the blank template to the new file
                    writer.close();
                }


                File mapSeeds = new File(path + File.separator + "mapSeeds.txt");
                if (override || !mapSeeds.exists()) { //If the mapSeeds file doesnt exist. Make and initialize it.
                    PrintWriter writer = new PrintWriter(new FileWriter(path + File.separator + "mapSeeds.txt", false));
                    writer.printf("%s", prepareBlockToWrite(readFileInsideJar("/mapSeeds.txt", true))); //Write the blank template to the new file
                    writer.close();
                }

            } else {
                System.out.println("Could not create directory");
            }
        }catch (IOException e) {
            System.out.println("\033[0;31m" + e.getMessage());
        }
    }

    private static String prepareBlockToWrite(ArrayList<ArrayList<Pair<String, String>>> lines) {
        String toWrite = "";
        for (ArrayList<Pair<String, String>> entry : lines) {
            toWrite += "<begin>\n";
            for (Pair<String, String> line : entry) {
                toWrite += line.getKey() + ":" + line.getValue() + "\n";
            }
            toWrite += "<end>\n\n";
        }
        return  toWrite;
    }


    public static void saveNewMapSeed(String name, long seed) {
        //Need to read the old scores, to write them back after adding
        String toWrite = "";
        try {
            ArrayList<ArrayList<Pair<String, String>>> seeds = readFile("mapSeeds.txt", true);

            //Shift the current saved seeds down 1 row
            seeds.get(0).set(2, seeds.get(0).get(1));
            seeds.get(0).set(1, seeds.get(0).get(0));
            if (name.length() > 0) {
                seeds.get(0).set(0, new Pair<>(name, Long.toString(seed)));
            } else {
                seeds.get(0).set(0, new Pair<>("Unnamed map", Long.toString(seed)));
            }

            toWrite = prepareBlockToWrite(seeds);

            String path = System.getProperty("user.home") + File.separator + "Group34GameSettings";
            PrintWriter writer = new PrintWriter(new FileWriter(path + File.separator + "mapSeeds.txt", false));
            writer.printf("%s", toWrite);
            writer.close();

        } catch (IOException e) {
            System.out.println("\033[0;31m" + e.getMessage());
        }
    }



}

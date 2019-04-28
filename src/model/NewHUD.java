package model;

import FXMLControllers.HUDController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

enum RoomType{
    VISITED,
    NO_ROOM,
    UNVISITED,
    CURRENT,
    BOSS_ROOM
}
public class NewHUD extends MenuElement{

    //DEPRECATED - assumes this class extends pause menu
    //Rough layout of menu from top to bottom
    /*private HBox statusElements;
    private VerticalHUDBar healthBar;
    private VerticalHUDBar energyBar;
    private ImageView heartLives;
    private VBox heartsBox;

    private HBox itemBox;
    private ImageView equippedItem;
    private Label itemLabel;

    private Minimap minimap;*/

    private HUDController controller;
    private Inventory inventory;

    private double currHealth;
    private double maxHealth;
    private double healthPercent;
    private double currEnergy;
    private double maxEnergy;
    private double energyPercent;
    private int minutes = 0;
    private int seconds = 0;
    private ArrayList<RoomType> rooms = new ArrayList<>();


    public NewHUD(String ID, double maxHealth, double maxEnergy, Inventory inventory, int xPos, int yPos) {
        super(ID,0,0, xPos, yPos);

        this.maxHealth = maxHealth;
        this.maxEnergy = maxEnergy;
        this.inventory = inventory;

        currHealth = maxHealth;
        currEnergy = 0;


        try{
            controller = LoadFXML();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.makeMap();
    }

    private HUDController LoadFXML() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/hud.fxml"));
        this.getChildren().setAll((AnchorPane) loader.load());
        return loader.getController();
    }

    public String updateTimer(){
        String toReturn = "";
        if (this.seconds < 59) {
            this.seconds++;
        } else {
            this.seconds = 0;
            this.minutes++;
        }

        if (this.seconds < 10) {
            toReturn += "0";
        }
        toReturn += this.seconds;

        if (this.minutes > 0) {
            toReturn = this.minutes + ":" + toReturn;
        }

        return toReturn;
    }

    private void makeMap() {
        int xOffset = 5;
        int yOffset = 450;
        ArrayList<ArrayList<Boolean>> levelLayout = Map.getLevelLayout();
        int width = 0;
        if (levelLayout != null && levelLayout.size() > 0) {
            width = levelLayout.get(0).size();
        }
        for (int row = 0; row < levelLayout.size(); row++) {
            for (int col = 0; col < levelLayout.get(row).size(); col++) {
                ImageView imageView;
                if (col + row * width == Map.tutorialLevelNumber) {
                    rooms.add(RoomType.CURRENT);
                    imageView = new ImageView(PreLoadedImages.mapCurrentRoom);
                } else if (Map.bossEntranceLevelNumber != -1 && col + row * width == Map.bossEntranceLevelNumber) {
                    rooms.add(RoomType.BOSS_ROOM);
                    imageView = new ImageView(PreLoadedImages.mapBossEnterance);
                } else if (levelLayout.get(row).get(col)) {
                    rooms.add(RoomType.UNVISITED);
                    imageView = new ImageView(PreLoadedImages.mapRoomUnVisited);
                } else {
                    rooms.add(RoomType.NO_ROOM);
                    imageView = new ImageView(PreLoadedImages.mapNoRoom);
                }
                this.getChildren().add((col + row * width) + 1, imageView);
                imageView.setY(yOffset + row * 19);
                imageView.setX(xOffset + col * 19);
            }
        }
    }

    private void updateMap(int currentLevel) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i) == RoomType.CURRENT) {
                rooms.set(i,RoomType.VISITED);
            }
        }

        if (currentLevel < rooms.size()) { //As boss room is lvl 8055, so check for out of bounds
            rooms.set(currentLevel, RoomType.CURRENT);
        }


        for (int i = 0; i < rooms.size(); i++) {
            ImageView imageView = (ImageView) this.getChildren().get(i + 1);//AnchorPane is element 0 so +1.
            if (this.rooms.get(i) == RoomType.CURRENT) {
                imageView.setImage(PreLoadedImages.mapCurrentRoom);
            } else if (this.rooms.get(i) == RoomType.UNVISITED) {
                imageView.setImage(PreLoadedImages.mapRoomUnVisited);
            } else if (this.rooms.get(i) == RoomType.VISITED) {
                imageView.setImage(PreLoadedImages.mapRoomVisited);
            } else if (this.rooms.get(i) == RoomType.NO_ROOM) {
                imageView.setImage(PreLoadedImages.mapNoRoom);
            } else if (this.rooms.get(i) == RoomType.BOSS_ROOM) {
                imageView.setImage(PreLoadedImages.mapBossEnterance);
            }
        }
    }

    public void setCurrHealth(double currHealth) {
        this.currHealth = currHealth;
    }

    public void setCurrEnergy(double currEnergy) {
        this.currEnergy = currEnergy;
    }

    public void setEquippedItem(Item item){
        controller.UpdateEquippedItem(item);
    }

    public int getMinutes(){
        return this.minutes;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void tick(int lives, int currentLevel){
        this.healthPercent = this.currHealth/this.maxHealth;
        this.energyPercent = this.currEnergy/this.maxEnergy;

        this.controller.UpdateHealth(this.healthPercent);
        this.controller.UpdateEnergy(this.energyPercent);
        this.controller.UpdateEquippedItem(this.inventory.getEquippedItem());
        this.controller.UpdateLives(lives);
        this.updateMap(currentLevel);

//        this.controller.updateMap();
    }


    /*private void CreateHUDLayout(int width, int height){

        //Create nodes of top part of HUD
        statusElements = new HBox(20);
        //healthBar = new VerticalHUDBar("healthBar",50,350,1,1);
        ProgressBar testBar = new ProgressBar();
        testBar.setPrefSize(50,350);
        energyBar = new VerticalHUDBar("energyBar",50,350,1,1);
        heartLives = new ImageView(new Image(this.getClass().getResourceAsStream("/sprites/healthKit.png")));
        heartsBox = new VBox(20);

        //Add top part to it's containers
        heartsBox.getChildren().add(heartLives);
        statusElements.getChildren().addAll(testBar,energyBar,heartsBox);

        //Create nodes of middle part of HUD
        itemBox = new HBox(100);
        equippedItem = new ImageView(new Image(this.getClass().getResourceAsStream("/sprites/emptyItemSlot.png")));
        itemLabel = new Label("ITEM");

        //Add middle part to it's container
        itemBox.getChildren().addAll(itemLabel,equippedItem);

        //idk about minimap yet, needs to be a node to work with everything else though
        //minimap = new Minimap(null);

        //Add all of the individual containers to the master VBox
        vbox.getChildren().addAll(statusElements,itemBox);
    }

    public VerticalHUDBar getHealthBar() {
        return healthBar;
    }

    public VerticalHUDBar getEnergyBar() {
        return energyBar;
    }*/
}

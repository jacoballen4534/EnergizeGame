package model;

import FXMLControllers.HUDController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

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

    private int seconds, minutes; //For updateTimer()

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

        //CreateHUDLayout(width,height); //Deprecated
    }

    private HUDController LoadFXML() throws IOException{
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxmls/hud.fxml")
        );
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

    public void tick(){

        healthPercent = currHealth/maxHealth;
        energyPercent = currEnergy/maxEnergy;

        controller.UpdateHealth(healthPercent);
        controller.UpdateEnergy(energyPercent);
        controller.UpdateEquippedItem(inventory.getEquippedItem());
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

package model;

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

public class NewHUD extends PauseMenu {

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

    public NewHUD(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        this.vbox.setAlignment(Pos.TOP_CENTER);
        try{LoadFXML();}
        catch (Exception e){
            e.printStackTrace();
        }
        //CreateHUDLayout(width,height);
    }

    private void LoadFXML() throws IOException{
        this.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResource("/fxmls/hud.fxml")));
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

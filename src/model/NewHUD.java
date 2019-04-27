package model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NewHUD extends PauseMenu {

    private NewHUDBar healthBar;
    private NewHUDBar energyBar;
    //private Label healthLabel;
    //private Label energyLabel;

    //private Minimap minimap;

    //private ImageView equippedItem;
    //private Label itemLabel;
    //private ImageView heartLives;

    //Containers
    private HBox hbox;
    private VBox heartsBox;

    public NewHUD(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        this.vbox.setAlignment(Pos.TOP_CENTER);
        CreateHUDLayout(width,height);
    }

    private void CreateHUDLayout(int width, int height){

        //Create all the necessary elements
        this.hbox = new HBox(50);
        this.hbox.setPrefSize(width,height);
        //this.heartsBox = new VBox(20);
        healthBar = new NewHUDBar("healthBar",50,300,100,100);
        energyBar = new NewHUDBar("energyBar",50,300,100,100);

        //Add them to their respective containers
        hbox.getChildren().addAll(healthBar,energyBar);
        this.vbox.getChildren().add(0,hbox);
    }

    public void tick(){

    }

    public NewHUDBar getHealthBar() {
        return healthBar;
    }

    public NewHUDBar getEnergyBar() {
        return energyBar;
    }
}

package model;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

public class NewHUD extends MenuElement {

    private ProgressBar healthBar;
    private ProgressBar energyBar;
    private Label healthLabel;
    private Label energyLabel;
    private ImageView equippedItem;
    private ImageView heartLives;

    public NewHUD(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
    }

}

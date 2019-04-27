package FXMLControllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Item;
import model.PreLoadedImages;

import java.net.URL;
import java.util.ResourceBundle;

public class HUDController implements Initializable {

    @FXML public ProgressBar healthBar;
    @FXML public ProgressBar energyBar;

    @FXML public ImageView heartContainer1;
    @FXML public ImageView heartContainer2;
    @FXML public ImageView heartContainer3;

    @FXML public ImageView equippedItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Default starting values
        heartContainer1.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
        heartContainer2.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
        heartContainer3.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
        equippedItem.setImage(new Image(this.getClass().getResourceAsStream("/sprites/emptyItemSlot.png")));
        healthBar.setProgress(1);
        energyBar.setProgress(1);
    }

    @FXML public void UpdateHealth(double healthPercent){
        healthBar.setProgress(healthPercent);
    }

    @FXML public void UpdateEnergy(double energyPercent){
        energyBar.setProgress(energyPercent);
    }

    @FXML public void UpdateEquippedItem(Item item){
        if (item != null) equippedItem.setImage(item.getJFXImage());
        else equippedItem.setImage(SwingFXUtils.toFXImage(PreLoadedImages.emptyItemSlot,null));
    }

    //TODO: Refactor to burn our eyes less
    @FXML public void UpdateLives(int lives){
        switch(lives){
            case 0:
                heartContainer1.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartEmpty.png")));
                heartContainer2.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartEmpty.png")));
                heartContainer3.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartEmpty.png")));
            case 1:
                heartContainer1.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
                heartContainer2.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartEmpty.png")));
                heartContainer3.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartEmpty.png")));
            case 2:
                heartContainer1.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
                heartContainer2.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
                heartContainer3.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartEmpty.png")));
            case 3:
                heartContainer1.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
                heartContainer2.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
                heartContainer3.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
            default:
                return;
        }
    }
}

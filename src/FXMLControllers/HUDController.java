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

    @FXML private ImageView heartContainer1;
    @FXML private ImageView heartContainer2;
    @FXML private ImageView heartContainer3;

    @FXML public ImageView equippedItem;
    protected int lives;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Default starting values
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
//        lives = 2;
        switch(lives){
            case 0:
                this.heartContainer1.setImage(SwingFXUtils.toFXImage(PreLoadedImages.emptyHeart,null));
                this.heartContainer2.setImage(SwingFXUtils.toFXImage(PreLoadedImages.emptyHeart,null));
                this.heartContainer3.setImage(SwingFXUtils.toFXImage(PreLoadedImages.emptyHeart,null));
                break;
            case 1:
                this.heartContainer1.setImage(SwingFXUtils.toFXImage(PreLoadedImages.fullHeart,null));
                this.heartContainer2.setImage(SwingFXUtils.toFXImage(PreLoadedImages.emptyHeart,null));
                this.heartContainer3.setImage(SwingFXUtils.toFXImage(PreLoadedImages.emptyHeart,null));
                break;

            case 2:
                this.heartContainer1.setImage(SwingFXUtils.toFXImage(PreLoadedImages.fullHeart,null));
                this.heartContainer2.setImage(SwingFXUtils.toFXImage(PreLoadedImages.fullHeart,null));
                this.heartContainer3.setImage(SwingFXUtils.toFXImage(PreLoadedImages.emptyHeart,null));
                break;

            case 3:
                this.heartContainer1.setImage(SwingFXUtils.toFXImage(PreLoadedImages.fullHeart,null));
                this.heartContainer2.setImage(SwingFXUtils.toFXImage(PreLoadedImages.fullHeart,null));
                this.heartContainer3.setImage(SwingFXUtils.toFXImage(PreLoadedImages.fullHeart,null));
                break;

            default:
                return;
        }
    }
}

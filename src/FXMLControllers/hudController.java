package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class hudController implements Initializable {

    @FXML public ProgressBar healthBar;
    @FXML public ProgressBar energyBar;

    @FXML public ImageView heartContainer1;
    @FXML public ImageView heartContainer2;
    @FXML public ImageView heartContainer3;

    @FXML public ImageView equippedItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        heartContainer1.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
        heartContainer2.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
        heartContainer3.setImage(new Image(this.getClass().getResourceAsStream("/sprites/heartFull.png")));
    }
}

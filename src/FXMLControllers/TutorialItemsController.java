package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TutorialItemsController implements Initializable {

    @FXML public AnchorPane tutorialScreenItemsPane;
    @FXML public ImageView healthKit;
    @FXML public ImageView energyKit;
    @FXML public ImageView windScroll;
    @FXML public ImageView iceScroll;
    @FXML public ImageView fireScroll;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        healthKit.setImage(new Image(this.getClass().getResourceAsStream("/sprites/healthKit.png")));
        energyKit.setImage(new Image(this.getClass().getResourceAsStream("/sprites/energyKit.png")));
        windScroll.setImage(new Image(this.getClass().getResourceAsStream("/sprites/windScroll.png")));
        iceScroll.setImage(new Image(this.getClass().getResourceAsStream("/sprites/iceScroll.png")));
        fireScroll.setImage(new Image(this.getClass().getResourceAsStream("/sprites/fireScroll.png")));
    }

    public void startGameButtonPressed(){
        System.out.println("Refactor note");
    }

    public void titleButtonPressed() throws IOException {
        tutorialScreenItemsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    public void nextPageButtonPressed() throws IOException{
        tutorialScreenItemsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/tutorialScreenMechanics.fxml")));
    }

    public void previousPageButtonPressed() throws IOException{
        tutorialScreenItemsPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/tutorialScreenControls.fxml")));
    }
}

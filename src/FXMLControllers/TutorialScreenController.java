package FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.Game;


public class TutorialScreenController implements Initializable {

    @FXML
    public AnchorPane tutorialScreenPane;

    @FXML public ImageView movementControls;
    @FXML public ImageView attackControl;
    @FXML public ImageView itemControl;
    /*@FXML public ImageView blockControl;
    @FXML public ImageView escapeButton;
    @FXML public ImageView pauseButton;*/

    private static Game game;
    public static MainMenuController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementControls.setImage(new Image(this.getClass().getResourceAsStream("/sprites/key_wasd.png")));
        attackControl.setImage(new Image(this.getClass().getResourceAsStream("/sprites/key_j.png")));
        itemControl.setImage(new Image(this.getClass().getResourceAsStream("/sprites/key_k.png")));
        /*blockControl.setImage(new Image(this.getClass().getResourceAsStream("/sprites/key_l.png")));
        pauseButton.setImage(new Image(this.getClass().getResourceAsStream("/sprites/key_p.png")));
        escapeButton.setImage(new Image(this.getClass().getResourceAsStream("/sprites/key_esc.png")));*/
    }

    public void continueButtonPressed(){
        game = new Game(controller, System.currentTimeMillis());
        game.start();
        controller.setGame(game);
        try{backButtonPressed();}
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Continue button pressed");
    }

    public void backButtonPressed() throws IOException {
       tutorialScreenPane.getChildren().setAll((AnchorPane) new FXMLLoader().load(getClass().getResourceAsStream("/fxmls/mainMenu.fxml")));
    }

    public static void setController(MainMenuController menuController){
        controller = menuController;
    }
}

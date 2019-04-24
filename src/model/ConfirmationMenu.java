package model;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import static sample.FXMLUtils.CreateButton;
import static sample.FXMLUtils.CreateLabel;

public class ConfirmationMenu extends PauseMenu{

    private HBox hbox;

    public ConfirmationMenu(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        this.hbox = new HBox(50);
        hbox.setAlignment(Pos.CENTER);
    }

    public HBox getHbox() {
        return hbox;
    }
}

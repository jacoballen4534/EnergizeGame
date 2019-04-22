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

public class ConfirmationMenu extends Menu{

    private VBox vbox;
    private HBox hbox;
    private Label titleLabel;
    private Button confirmButton;
    private Button cancelButton;

    public ConfirmationMenu(String title, String ID, int width, int height, int xPos, int yPos,
                            EventHandler confirmationAction) {
        super(ID, width, height, xPos, yPos);
        this.vbox = new VBox(10);
        this.vbox.setAlignment(Pos.CENTER);
        this.hbox = new HBox(10);
        this.titleLabel = CreateLabel(title,"titleLabel",200,100,
                TextAlignment.CENTER,true);
        this.confirmButton = CreateButton("Yes","confirmButton",
                100,50,confirmationAction);
        this.cancelButton = CreateButton("No","cancelButton",
                100,50,mouseEvent->this.hide());
        this.hbox.getChildren().addAll(confirmButton,cancelButton);
        this.vbox.getChildren().addAll(titleLabel,hbox);
        this.getChildren().add(vbox);
    }
}

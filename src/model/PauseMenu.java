package model;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PauseMenu extends Menu {

    private VBox vbox;

    public PauseMenu(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        this.vbox = new VBox(10);
        this.vbox.setAlignment(Pos.CENTER);
        this.getChildren().add(vbox);
    }

    public void AddButton(int pos, String text, String id, int width, int height, EventHandler onClick){
        Button button = new Button(text);
        button.setId(id);
        button.setPrefSize(width,height);
        button.setOnMouseClicked(onClick);
        this.vbox.getChildren().add(pos,button);
    }

    public void RemoveNodeById(String id){
        //this.vbox.getChildren().remove(getNodeById(this.vbox,id)); //Need to make this method universally accessible
    }

    public void AddLabel(int pos, String text, String id){
        Label label = new Label(text);
        label.setId(id);
        this.vbox.getChildren().add(pos,label);
    }
}

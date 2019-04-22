package model;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.Node;

import static sample.FXMLUtils.getNodeByID;

public class PauseMenu extends Menu {

    protected VBox vbox;

    public PauseMenu(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        this.vbox = new VBox(10);
        this.vbox.setAlignment(Pos.CENTER);
        this.vbox.setPrefSize(width,height);
        this.getChildren().add(vbox);
    }

    public void AddNodeToVBox(int pos, Node node){
        this.vbox.getChildren().add(pos,node);
    }

    public void RemoveNodeById(String id){
        this.vbox.getChildren().remove(getNodeByID(id,this.vbox));
    }

    public void SetLabelAsTitle(Label label){
        this.vbox.getChildren().add(0,label);
    }
}

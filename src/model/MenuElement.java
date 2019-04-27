package model;

import javafx.scene.layout.AnchorPane;

public class MenuElement extends AnchorPane {

    public MenuElement(String ID, int width, int height, int xPos, int yPos) {
        super();
        this.setId(ID);
        this.setPrefSize(width,height);
        this.setLayoutX(xPos-width/2);
        this.setLayoutY(yPos-height/2);
        this.getStyleClass().setAll("anchorpane");
        this.setVisible(false);
    }

    public void show(){this.setVisible(true);}
    public void hide(){this.setVisible(false);}

}
package model;

import javafx.scene.layout.AnchorPane;

public class Menu extends AnchorPane {

    public Menu(int width, int height, int xPos, int yPos) {
        super();
        this.setPrefSize(width,height);
        this.setLayoutX(xPos-width/2);
        this.setLayoutY(yPos-height/2);
        //this.setVisible(true);
    }

    public void show(){super.setVisible(true);} //Doesn't modify visible property for some reason
    public void hide(){super.setVisible(false);} //Doesn't modify visible property for some reason

}
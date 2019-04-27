package model;

import javafx.scene.control.ProgressBar;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

//Vertical progress bar for the side pane
public class NewHUDBar extends ProgressBar {

    private int currVal, maxVal;

    public NewHUDBar(String ID, int width, int height, int currVal, int maxVal) {
        super();
        this.setId(ID);
        this.setPrefSize(height,width);
        this.getTransforms().setAll(
                new Translate(0,height),
                new Rotate(-90,0,0)
        );
        this.currVal = currVal;
        this.maxVal = maxVal;
    }

    public int getCurrVal() {
        return currVal;
    }

    public void setCurrVal(int currVal) {
        this.currVal = currVal;
    }

    public int getMaxVal() {
        return maxVal;
    }
}

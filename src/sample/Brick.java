package sample;

import javafx.geometry.Point2D;
import javafx.scene.shape.Box;

public class Brick extends Box {
    private final Point2D position;
    private final static int width = 100;
    private final static int height = 30;

    public Brick(double x, double y) { //This is top left of brick. Need to expand in right and down directions
        this.position = new Point2D(x,y);

    }

    public static Point2D getSize() {
        return new Point2D(Brick.width, Brick.height);
    }
}

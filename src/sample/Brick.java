package sample;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;
import java.util.HashMap;

public class Brick extends Box {
    private final Point2D location;
    private final static int width = 100;
    private final static int height = 30;
    private HashMap<Border.WallLocation, Double> bounds;


    public Brick(double x, double y) { //This is top left of brick. Need to expand in right and down directions
        super(Brick.width-6, Brick.height-6, 30); //Subtract for spacing around each brick
        this.location = new Point2D(x,y);
        this.setMaterial(new PhongMaterial(Color.ORANGE));
        this.setLocation(this.location);
        this.bounds = new HashMap<>();
        this.bounds.put(Border.WallLocation.Top, this.location.getY() - height/2);
        this.bounds.put(Border.WallLocation.Bottom, this.location.getY() + height/2);
        this.bounds.put(Border.WallLocation.Left, this.location.getX() - width/2);
        this.bounds.put(Border.WallLocation.Right, this.location.getX() + width/2);
    }


    public void setLocation(Point2D position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
    }

    public static Point2D getSize() {
        return new Point2D(Brick.width, Brick.height);
    }

    public HashMap<Border.WallLocation, Double> getBounds() {
        return this.bounds;
    }

    public Border.WallLocation findHitSide(Point2D ballLocation, double radius) {
        if (ballLocation.getX() - radius <= this.bounds.get(Border.WallLocation.Right) && ballLocation.getX() + radius >= this.bounds.get(Border.WallLocation.Left)) {
            if (ballLocation.getY() - radius >= this.bounds.get(Border.WallLocation.Bottom)) {
                return Border.WallLocation.Bottom;
            } else {
                return Border.WallLocation.Top;
            }
        } else {
            if (ballLocation.getX() - radius >= this.bounds.get(Border.WallLocation.Right)) {
                return Border.WallLocation.Right;
            } else {
                return Border.WallLocation.Left;
            }
        }
    }

    public void hit () {
        Group root = (Group)this.getParent();
        root.getChildren().remove(this);
    }

}

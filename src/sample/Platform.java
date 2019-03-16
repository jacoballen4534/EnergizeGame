package sample;

import javafx.application.ConditionalFeature;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;


public class Platform extends Box {
    private Point2D location;
    private static final Point2D size = new Point2D(100, 10);
    private Point2D direction = new Point2D(0, 0);


    public Platform(double x, double y, Color color){ //This is centre of platform need to expand in all directions
        super(size.getX(), size.getY(),60);
        this.setMaterial(new PhongMaterial(color));
        this.location = new Point2D(x, y);
        this.setLocation(this.location);
    }

    public static Point2D getSize() {
        return Platform.size;
    }

    public void setLocation(Point2D position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
    }

    public void setXDirection(Point2D direction) {
        this.direction = direction;
    }

    public void Update(long timePassed, ArrayList<Border> borders) {
        Point2D nextPos = this.location.add(this.direction.multiply(timePassed / 1e6));

        if(this.intersects(nextPos, borders) == Border.WallLocation.Null) {
            this.location = nextPos;
        }

        this.setLocation(this.location);
    }

    private Border.WallLocation intersects(Point2D position, ArrayList<Border> borders) {

        for (Border border : borders) {
            HashMap<Border.WallLocation, Double> borderBounds = border.getBounds();
            if (position.getX() - Platform.size.getX() / 2 <= borderBounds.get(Border.WallLocation.Right) &&
                    position.getX() + Platform.size.getX() / 2 >= borderBounds.get(Border.WallLocation.Left) &&
                    position.getY() - Platform.size.getY()/2 <= borderBounds.get(Border.WallLocation.Bottom) &&
                    position.getY() + Platform.size.getY()/2 >= borderBounds.get(Border.WallLocation.Top)) {
                return border.getWallLocation();
            }
        }

        return Border.WallLocation.Null;
    }
}

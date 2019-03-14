package sample;

import javafx.scene.shape.Box;

import javafx.geometry.Point2D;


public class Platform extends Box {
    private Point2D location;
    private static final Point2D size = new Point2D(200, 30);
    private Point2D direction = new Point2D(0, 0);
    private Point2D nextPoint;




    public Platform(double x, double y){ //This is centre of platform need to expand in all directions
        super(size.getX(), size.getY(),1);
        location = new Point2D(x, y);
    }

    public static Point2D getSize() {
        return Platform.size;
    }

    public void setLocation(Point2D position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
    }

    public Point2D getDirection() {
        return this.direction;
    }

    public void setXDirection(Point2D direction) {
        this.direction = direction;
    }

    public void platformUpdate (long timePassed) {
        nextPoint = this.location.add(direction);
        this.location = nextPoint;
        this.setLocation(this.location);
    }
}

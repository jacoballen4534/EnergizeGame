package sample;

import javafx.application.ConditionalFeature;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import javafx.geometry.Point2D;

import java.util.ArrayList;


public class Platform extends Box {
    private Point2D location;
    private static final Point2D size = new Point2D(100, 10);
    private Point2D direction = new Point2D(0, 0);


    public Platform(double x, double y, Color color){ //This is centre of platform need to expand in all directions
        super(size.getX(), size.getY(),60);
        this.setMaterial(new PhongMaterial(color));
        location = new Point2D(x, y);
        this.setLocation(location);
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
        Point2D backupPosition = this.location;
        this.location = this.location.add(this.direction.multiply(timePassed / 1e6));

        switch (this.intersects(borders)) {
            case Left:
                this.location = backupPosition.add(new Point2D(Platform.size.getX()/4, 0));
                break;
            case Right:
                this.location = backupPosition.subtract(new Point2D(Platform.size.getX()/4, 0));
                break;
        }
        this.setLocation(this.location);
    }

    private Border.WallLocation intersects(ArrayList<Border> borders) {
        for (Border border : borders) {
            if (border.intersects(border.sceneToLocal(this.localToScene(this.getBoundsInLocal())))) {
                return border.getWallLocation();
            }
        }
        return Border.WallLocation.Null;
    }
}

package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Border extends Box {

    public enum WallLocation {
        Top,
        Bottom,
        Left,
        Right,
        Null
    }

    private WallLocation wallLocation;

    public Border(Point2D origin, Point3D size, WallLocation location) {
        super(size.getX(), size.getY(), size.getZ());
        this.setMaterial(new PhongMaterial(Color.BLACK));
        this.setTranslateX(origin.getX());
        this.setTranslateY(origin.getY());
        this.wallLocation = location;
    }

    public WallLocation getWallLocation() {
        return this.wallLocation;
    }
}

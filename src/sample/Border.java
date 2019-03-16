package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Border extends Box {

    public enum WallLocation {
        Top,
        Bottom,
        Left,
        Right,
        Null
    }

    private WallLocation wallLocation;
    private Point2D origin;
    private Point3D size;
    private HashMap<WallLocation, Double> bounds;

    public Border(Point2D origin, Point3D size, WallLocation location) {
        super(size.getX(), size.getY(), size.getZ());
        this.setMaterial(new PhongMaterial(Color.BLACK));
        this.origin = origin;
        this.size = size;
        this.setTranslateX(origin.getX());
        this.setTranslateY(origin.getY());
        this.wallLocation = location;

        this.bounds = new HashMap<>();
        this.bounds.put(WallLocation.Top, this.origin.getY() - size.getY()/2);
        this.bounds.put(WallLocation.Bottom, this.origin.getY() + size.getY()/2);
        this.bounds.put(WallLocation.Left, this.origin.getX() - size.getX()/2);
        this.bounds.put(WallLocation.Right, this.origin.getX() + size.getX()/2);
    }

    public WallLocation getWallLocation() {
        return this.wallLocation;
    }

    public HashMap<Border.WallLocation, Double> getBounds() {
        return this.bounds;
    }
}

package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Ball extends Sphere {

    private Point2D origin;
    private double radius;

    Ball(Point2D origin, double radius) {
        super(radius);
        this.setMaterial(new PhongMaterial(Color.RED));
        this.setTranslateX(origin.getX());
        this.setTranslateY(origin.getY());
        this.radius = radius;
        this.origin = origin;

    }
}
package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Ball extends Sphere {

    private Point2D location;
    private static double radius = 10;
    private Point2D direction;

    Ball(Point2D location) {
        super(Ball.radius);
        this.setMaterial(new PhongMaterial(Color.RED));
        this.location = location;
        this.setLocation(this.location);
        Random rand = new Random();
        this.direction = new Point2D(rand.nextDouble()*2 - 1, -rand.nextDouble());
    }

    public void setLocation(Point2D position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
    }

    public static double getRadus() {
        return Ball.radius;
    }


    public void Update(long timePassed, ArrayList<Border> borders, ArrayList<Brick> bricks) {
        Point2D nextPos = this.location.add(this.direction.multiply(0.5));
        boolean hitWall = true;

        switch (this.intersects(borders, nextPos)) {
            case Null:
                hitWall = false;
                break;
            case Left:
                this.direction = new Point2D(-this.direction.getX(), this.direction.getY());
                break;
            case Right:
                this.direction = new Point2D(-this.direction.getX(), this.direction.getY());
                break;
            case Top:
                this.direction = new Point2D(this.direction.getX(), -this.direction.getY());
                break;
        }

        Brick intersectedBrick = intersects(nextPos, bricks);
        if (intersectedBrick != null) {
            //TODO: Find which side of the box was hit, to reflect in the correct direction.
        }



        this.setLocation(this.location);
    }

    private Border.WallLocation intersects(ArrayList<Border> borders, Point2D position) {

        for (Border border : borders) {
            HashMap<Border.WallLocation, Double> borderBounds = border.getBounds();

            if (position.getX() - Ball.radius <= borderBounds.get(Border.WallLocation.Right) &&
                    position.getX() + Ball.radius >= borderBounds.get(Border.WallLocation.Left) &&
                    position.getY() - Ball.radius <= borderBounds.get(Border.WallLocation.Bottom) &&
                    position.getY() + Ball.radius >= borderBounds.get(Border.WallLocation.Top)) {
                return border.getWallLocation();
            }
        }
        return Border.WallLocation.Null;
    }

    private Brick intersects(Point2D position, ArrayList<Brick> bricks) {

        for (Brick brick : bricks) {
            HashMap<Border.WallLocation, Double> borderBounds = brick.getBounds();

            if (position.getX() - Ball.radius <= borderBounds.get(Border.WallLocation.Right) &&
                    position.getX() + Ball.radius >= borderBounds.get(Border.WallLocation.Left) &&
                    position.getY() - Ball.radius <= borderBounds.get(Border.WallLocation.Bottom) &&
                    position.getY() + Ball.radius >= borderBounds.get(Border.WallLocation.Top)) {
                return brick;
            }
        }
        return null;
    }

}
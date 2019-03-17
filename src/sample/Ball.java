package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Ball extends Sphere {

    private Point2D location;
    private static double radius = 10;
    private Point2D direction;
    private  double velocity = 0.5;

    Ball(Point2D location) {
        super(Ball.radius);
        this.setMaterial(new PhongMaterial(Color.RED));
        this.location = location;
        this.setLocation(this.location);
        Random rand = new Random();
        this.direction = new Point2D(rand.nextDouble()*2 - 1, -rand.nextDouble()).normalize();
    }

    public void setLocation(Point2D position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
    }

    public static double getRadus() {
        return Ball.radius;
    }


    public void Update(long timePassed, ArrayList<Border> borders, ArrayList<Brick> bricks, Platform platform) throws Exception {
        Point2D nextPos = this.location.add(this.direction.multiply(Math.min(timePassed / 1e6, 4) * velocity));
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
            case Bottom:
                throw new Exception("GameOver");
                //TODO:GAMEOVER
        }

        boolean hitBrick = false;
        Brick intersectedBrick = intersects(nextPos, bricks);

        if (intersectedBrick != null) {
            intersectedBrick.hit();
            bricks.remove(intersectedBrick);
            hitBrick = true;
            switch (intersectedBrick.findHitSide(this.location, Ball.radius)) {
                case Left:
                    this.direction = new Point2D(-this.direction.getX(), this.direction.getY());
                    break;
                case Right:
                    this.direction = new Point2D(-this.direction.getX(), this.direction.getY());
                    break;
                case Top:
                    this.direction = new Point2D(this.direction.getX(), -this.direction.getY());
                    break;
                case Bottom:
                    this.direction = new Point2D(this.direction.getX(), -this.direction.getY());
                    break;
            }
        }

        boolean hitPlatform = false;
        if (intersects(nextPos, platform)) {
            hitPlatform = true;
            this.direction = new Point2D(this.direction.getX(), -this.direction.getY());
        }

        if (!hitWall && !hitBrick && !hitPlatform) {
            this.location = nextPos;
        }



        this.setLocation(this.location);
    }

    //intersects with any borders
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

    //intersects with any of the bricks
    private Brick intersects(Point2D position, ArrayList<Brick> bricks) {

        for (Brick brick : bricks) {
            HashMap<Border.WallLocation, Double> brickBounds = brick.getBounds();

            if (position.getX() - Ball.radius <= brickBounds.get(Border.WallLocation.Right) &&
                    position.getX() + Ball.radius >= brickBounds.get(Border.WallLocation.Left) &&
                    position.getY() - Ball.radius <= brickBounds.get(Border.WallLocation.Bottom) &&
                    position.getY() + Ball.radius >= brickBounds.get(Border.WallLocation.Top)) {
                return brick;
            }
        }
        return null;
    }

    //intersects with the platform
    private boolean intersects(Point2D location, Platform platform) {
        return (location.getX() <= platform.getLocation().getX() + Platform.getSize().getX() &&
                location.getX() >= platform.getLocation().getX() - Platform.getSize().getX() &&
                location.getY() + Ball.radius >= platform.getLocation().getY() - Platform.getSize().getY());
    }

}
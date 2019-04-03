package model;

public class Camera {

    private double x, y;

    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject gameObject, double windowWidth, double windowHeight){
        this.x += ((gameObject.getX() - x) - windowWidth/2) * 0.05f;
        this.y += ((gameObject.getY() - y) - windowHeight/2) * 0.05f;

        if (this.x <= 0) this.x = 0;
        if (this.x >= windowWidth) this.x = windowWidth;
        if (this.y <= 0) this.y = 0;
        if (this.y >= windowHeight) this.y = windowHeight;


    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}

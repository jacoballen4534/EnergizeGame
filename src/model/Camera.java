package model;

public class Camera {

    private double x, y;

    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject gameObject, double windowWidth, double windowHeight, int levelWidth, int levelHeight){
        this.x += ((gameObject.getX() - x) - windowWidth/2) * 0.05f;
        this.y += ((gameObject.getY() - y) - windowHeight/2) * 0.05f;

        if (this.x <= 0) this.x = 0;
        if (this.x >= levelWidth - windowWidth) this.x = levelWidth - windowWidth;
        if (this.y <= 0) this.y = 0;
        if (this.y >= levelHeight - windowHeight) this.y = levelHeight - windowHeight;

    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}

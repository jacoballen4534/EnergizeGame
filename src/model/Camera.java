package model;

public class Camera {

    private double x, y;

    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject gameObject, double windowWidth, double windowHeight, int levelWidth, int levelHeight){
        //TODO: Can use this to pan towards the boss/item of interest when entering a room
        //Update the camera to keep the protagonist in the centre of the screen but *0.05 so it doesnt snap to them
        this.x += ((gameObject.getX() - this.x) - windowWidth/2) * 0.05f;
        this.y += ((gameObject.getY() - this.y) - windowHeight/2) * 0.05f;

        //Clamp the camera to the edges of the map
        if (this.x <= 0) this.x = 0;
        if (this.x >= levelWidth - windowWidth) this.x = levelWidth - windowWidth;
        if (this.y <= 0) this.y = 0;
        if (this.y >= levelHeight - windowHeight) this.y = levelHeight - windowHeight;

    }

    public boolean panTransition (double windowWidth, double windowHeight, double destinationX, double destinationY) {
        double moveX = (destinationX - this.x) - windowWidth/2;
        double moveY = (destinationY - this.y) - windowHeight/2;

        this.x += (moveX) * 0.01f;
        this.y += (moveY) * 0.01f;
        return this.x == destinationX && this.y == destinationY;
    }

    public void setX (double x) {
        this.x = x;
    }
    public void setY (double y) {
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}

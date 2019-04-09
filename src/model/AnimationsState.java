package model;

public class AnimationsState {
    private double leftBorder;
    private double rightBorder;
    private double topBorder;
    private double bottomBorder;

    public AnimationsState(int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.topBorder = topBorder;
        this.bottomBorder = bottomBorder;
    }

    public AnimationsState() {
        this.leftBorder = 0;
        this.rightBorder = 0;
        this.topBorder = 0;
        this.bottomBorder = 0;
    }

    public void copy(AnimationsState toCopy) {
        this.leftBorder = toCopy.leftBorder;
        this.rightBorder = toCopy.rightBorder;
        this.topBorder = toCopy.topBorder;
        this.bottomBorder = toCopy.bottomBorder;
    }

    public double getLeftBorder() {
        return this.leftBorder;
    }

    public double getRightBorder() {
        return this.rightBorder;
    }

    public double getTopBorder() {
        return this.topBorder;
    }

    public double getBottomBorder() {
        return this.bottomBorder;
    }

    public void flipBoundingBoxX() {
        double temp = this.leftBorder;
        this.leftBorder = this.rightBorder;
        this.rightBorder = temp;
    }




}

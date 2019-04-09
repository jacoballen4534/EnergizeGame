package model;

public class BoundingBorder {
    private int leftBorder;
    private int rightBorder;
    private int topBorder;
    private int bottomBorder;

    public BoundingBorder(int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.topBorder = topBorder;
        this.bottomBorder = bottomBorder;
    }

    public BoundingBorder() {
        this.leftBorder = 0;
        this.rightBorder = 0;
        this.topBorder = 0;
        this.bottomBorder = 0;
    }

    public void copy(BoundingBorder toCopy) {
        this.leftBorder = toCopy.leftBorder;
        this.rightBorder = toCopy.rightBorder;
        this.topBorder = toCopy.topBorder;
        this.bottomBorder = toCopy.bottomBorder;
    }

    public int getLeftBorder() {
        return this.leftBorder;
    }

    public int getRightBorder() {
        return this.rightBorder;
    }

    public int getTopBorder() {
        return this.topBorder;
    }

    public int getBottomBorder() {
        return this.bottomBorder;
    }

    public void flipBoundingBoxX() {
        int temp = this.leftBorder;
        this.leftBorder = this.rightBorder;
        this.rightBorder = temp;
    }




}

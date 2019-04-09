package model;

public class AnimationsState {
    private double leftBorder;
    private double rightBorder;
    private double topBorder;
    private double bottomBorder;
    private int animationMaxCol; //Last frame of the animation
    private int animationRow; //Stays constant, this is just the row to cycle along.
    private int resetCol; //This is what it resets back to after the last frame of animation.


    public AnimationsState(int leftBorder, int rightBorder, int topBorder, int bottomBorder, int animationMaxCol, int animationRow, int resetCol) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.topBorder = topBorder;
        this.bottomBorder = bottomBorder;
        this.animationMaxCol = animationMaxCol;
        this.animationRow = animationRow;
        this.resetCol = resetCol;
    }

    public AnimationsState() {
        this.leftBorder = 0;
        this.rightBorder = 0;
        this.topBorder = 0;
        this.bottomBorder = 0;
        this.animationMaxCol = 0;
        this.animationRow = 0;
        this.resetCol = 0;
    }

    public int updateAnimationSprite(int animationCol) {
        if (animationCol < this.resetCol) {
            animationCol = this.resetCol;
        } else if (animationCol < this.animationMaxCol) {
            animationCol++;
        } else {
            animationCol = this.resetCol;
        }
        return animationCol;
    }

    public int getAnimationRow() {
        return this.animationRow;
    }

    public void copy(AnimationsState toCopy) {
        this.leftBorder = toCopy.leftBorder;
        this.rightBorder = toCopy.rightBorder;
        this.topBorder = toCopy.topBorder;
        this.bottomBorder = toCopy.bottomBorder;
        this.animationMaxCol = toCopy.animationMaxCol;
        this.animationRow = toCopy.animationRow;
        this.resetCol = toCopy.resetCol;
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

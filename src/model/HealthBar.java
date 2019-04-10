package model;

import javafx.scene.shape.Rectangle;

public class HealthBar {

    private float currHealth;
    private float maxHealth;
    private float healthPercentage;
    private Rectangle health2D;

    public HealthBar(float currHealth, float maxHealth) {
        this.currHealth = currHealth;
        this.maxHealth = maxHealth;
        this.healthPercentage = currHealth/maxHealth;
        health2D = new Rectangle(10,10,healthPercentage*100,5);
    }

    public void setCurrHealth(float currHealth) {
        this.currHealth = currHealth;
    }

    public float getHealthPercentage() {
        return healthPercentage;
    }

    public Rectangle getHealth2D() {
        return health2D;
    }
}

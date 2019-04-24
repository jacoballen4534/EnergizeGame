package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public abstract class Item extends GameObject{

    //Adding properties for compliance with InventoryMenu table
    protected String name;
    protected String description;
    protected int quantity;
    protected boolean inInventory;
    protected ImageView icon;

    public Item(String name, String description, int xLocation, int yLocation, BufferedImage sprite, int spriteWidth, int spriteHeight) {
        super(xLocation, yLocation, sprite, spriteWidth, spriteHeight, spriteWidth, spriteHeight);
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0),null);
        icon = new ImageView();
        icon.setFitHeight(50);
        icon.setFitWidth(50);
        icon.setImage(this.jfxImage);

        setName(name);
        setDescription(description);
        this.quantity = 0;
        this.inInventory = false;
        this.isSolid = false;
    }

    public abstract void useItem(Protagonist user);

    public ImageView getIcon(){
        return this.icon;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name= name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public Image getJFXImage() {
        return this.jfxImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void increaseQuantity(){
        quantity++;
    }

    public void decreaseQuantity(){
        quantity--;
    }

    public boolean isInInventory() {
        return inInventory;
    }

    public void setInInventory(boolean inInventory) {
        this.inInventory = inInventory;
    }
}

package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public abstract class Item extends GameObject{

    //Adding properties for compliance with InventoryMenu table
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty description = new SimpleStringProperty();
    private SimpleObjectProperty<Image> icon;

    public Item(String name, String description, int xLocation, int yLocation, BufferedImage sprite, int spriteWidth, int spriteHeight) {
        super(xLocation, yLocation, sprite, spriteWidth, spriteHeight, spriteWidth, spriteHeight);
        this.jfxImage = SwingFXUtils.toFXImage(this.spriteSheet.getSprite(0,0),null);
        icon = new SimpleObjectProperty<Image>(this.jfxImage);
        setName(name);
        setDescription(description);
        this.isSolid = false;
    }

    public abstract void useItem();

    public Image getImage(){
        return this.jfxImage;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Image getIcon() {
        return icon.get();
    }

    public SimpleObjectProperty<Image> iconProperty() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon.set(icon);
    }
}

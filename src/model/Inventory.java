package model;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class Inventory{

    private ArrayList<Item> items;
    private Item equippedItem;

    public Inventory(){
        items = new ArrayList<>();
    }

    public void addItem(Item item){
        if (this.equippedItem == null) {
            this.equippedItem = item;
        } else {
            Item getItem = hasItem(item);
            if (getItem == null) {
                item.increaseQuantity();
                items.add(item);
            }
            else increaseQuantity(getItem);
        }
    }

    public Item getEquippedItem() {
        return equippedItem;
    }

    public void setEquippedItem(Item equippedItem) {
        this.equippedItem = equippedItem;
    }

    public void changeEquippedItem(Item item){

        //Don't bother if the items are the same
        if (equippedItem.getName().equals(item.getName())) return;
        //Check that the item actually exists in the inventory
        if (hasItem(item) == null) return;

        addItem(equippedItem); //Adds a copy of the equippedItem to inventory
        equippedItem = item; //Update equipped item
        //Find item in item list and decrease amount/remove it
        items.forEach(item1 -> {
            if (item1.getName().equals(item.getName())){
                item1.decreaseQuantity();
                if (item1.getQuantity() == 0) removeItem(item1);
            }
        });
    }

    public void removeItem(Item item){
        items.forEach(item1 -> {
           if (item1.getName().equals(item.getName())){
               Platform.runLater(()->items.remove(item1));
           }
        });
    }

    public ArrayList<Item> getItemList(){
        return this.items;
    }

    public int getItemCount(){
        return items.size();
    }

    private Item hasItem(Item item){
        for (Item item1 : items){
            if(item1.getName().equals(item.getName()))
                return item1;
        }
        return null;
    }

    private void increaseQuantity(Item item){
        items.forEach(item1 -> {
            if (item1.getName().equals(item.getName()))
                item1.increaseQuantity();
        });
    }
}

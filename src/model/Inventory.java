package model;

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

    public void removeItem(Item item){
        this.items.remove(item);
    }

    public ArrayList<Item> getItemList(){
        return this.items;
    }

    public int getItemCount(){
        return items.size();
    }

    /*public boolean containsItem(Item item){
        return items.contains(item);
    }*/

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

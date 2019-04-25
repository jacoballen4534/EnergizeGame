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

        /*
        Possible cases:
        - 'item' is the same as the currently equipped item
        - 'item' does not exist in inventory
        - There is more than one copy of the equipped item
        - There is only one copy of the equipped item
        Additionally;
        - 'item' may only have a quantity of 1 (so it should be removed from the inventory)
        - 'item' has a quantity of >1 (so a copy remains in inventory)
        * */

        //Don't bother if the items are the same
        if (equippedItem.getName().equals(item.getName())) return;
        //Check that the item actually exists in the inventory
        if (hasItem(item) == null) return;

        //At this point, an item switch will need to occur
        addItem(equippedItem); //Adds a copy of the equippedItem to inventory, initial condition skipped
        equippedItem = item; //Update equipped item
        Item itemInInventory = hasItem(item); //Try and interact with the item within the ArrayList
        itemInInventory.decreaseQuantity(); //Reduce item amount
        if (item.getQuantity() == 0) removeItem(item); //Remove if amount == 0

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

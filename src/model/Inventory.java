package model;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class Inventory{

    private ArrayList<Item> items;
    private Item equippedItem;
    private int pos = 0;

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

        //Check that the item actually exists in the inventory
        if (hasItem(item) == null) return;

        //Don't bother if the items are the same
        if (equippedItem != null && equippedItem.getName().equals(item.getName())) {
            return;
        }
        else {
            if (equippedItem != null) addItem(equippedItem); //Adds a copy of the equippedItem to inventory
            equippedItem = item; //Update equipped item
            removeItem(item);
        }
    }

    public void updateEquippedItem(){
        /*
        * Options
        * 1. Equipped item is the only copy of the item (total items > 0)
        * 2. Equipped item is no the only copy of the item (total items > 0)
        * 3. Equipped item is the only item (items = 0)
        * */
        if (getItemCount() > 0){
            if (hasItem(equippedItem) != null){
                removeItem(equippedItem);
            }
            else{
                equippedItem = this.items.get(0);
            }
        }
        else{
            equippedItem = null;
        }
    }

    //Item scrolling
    public Item getNextItem(){
        //int pos = items.indexOf(equippedItem);
        //Item nextItem = (pos==items.size()) ? items.get(0):items.get(pos+1);
        pos = (pos==items.size()) ? 0:pos+1;
        Item nextItem = items.get(pos);
        return nextItem;
    }

    public void removeItem(Item item) throws NullPointerException{
        if (item == null) throw new NullPointerException("Tried to remove null from inventory");
        //Find item in item list and decrease amount/remove it
        items.forEach(item1 -> {
            if (item1.getName().equals(item.getName())){
                item1.decreaseQuantity();
                if (item1.getQuantity() == 0) Platform.runLater(()->items.remove(item1));
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
        if (item == null) return null;
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

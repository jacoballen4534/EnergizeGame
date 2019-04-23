package model;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class Inventory{

    private ArrayList<Item> items;
    private int size;

    public Inventory(int size){
        items = new ArrayList<>();
        this.size = size;
    }

    public void addItem(Item item){
        System.out.println(items.add(item));
        System.out.println(items.size());
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public ArrayList<Item> getItemList(){
        return this.items;
    }

    public int size(){
        return size;
    }

    public int getItemCount(){
        return items.size();
    }

    public boolean isFull(){
        return items.size() == size;
    }

    public boolean containsItem(Item item){
        return items.contains(item);
    }
}

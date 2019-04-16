package model;

import java.util.ArrayList;

public class Inventory{

    private ArrayList<Item> items;
    private int size;

    public Inventory(int size){
        items = new ArrayList<Item>();
        this.size = size;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
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
}

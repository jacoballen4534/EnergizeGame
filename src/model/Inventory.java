package model;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;

public class Inventory{

    private ObservableList<Item> items;
    private int size;

    public Inventory(int size){
        items = new ObservableList<Item>() {
            @Override
            public void addListener(ListChangeListener<? super Item> listChangeListener) {

            }

            @Override
            public void removeListener(ListChangeListener<? super Item> listChangeListener) {

            }

            @Override
            public boolean addAll(Item... items) {
                return false;
            }

            @Override
            public boolean setAll(Item... items) {
                return false;
            }

            @Override
            public boolean setAll(Collection<? extends Item> collection) {
                return false;
            }

            @Override
            public boolean removeAll(Item... items) {
                return false;
            }

            @Override
            public boolean retainAll(Item... items) {
                return false;
            }

            @Override
            public void remove(int i, int i1) {

            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Item> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] ts) {
                return null;
            }

            @Override
            public boolean add(Item item) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Item> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, Collection<? extends Item> collection) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Item get(int i) {
                return null;
            }

            @Override
            public Item set(int i, Item item) {
                return null;
            }

            @Override
            public void add(int i, Item item) {

            }

            @Override
            public Item remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Item> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Item> listIterator(int i) {
                return null;
            }

            @Override
            public List<Item> subList(int i, int i1) {
                return null;
            }

            @Override
            public void addListener(InvalidationListener invalidationListener) {

            }

            @Override
            public void removeListener(InvalidationListener invalidationListener) {

            }
        };
        this.size = size;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public ObservableList getItemList(){
        return items;
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

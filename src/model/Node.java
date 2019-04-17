package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    private int id;
    private ArrayList<Integer> neighbors = new ArrayList<>(); //Dont need to hold weight as they are all 1
    private ArrayList<Node> nodeNeighbors = new ArrayList<>();

    Node(int id) {
        this.id = id;
    }

    public void addNighbor(int neighborId) {
        this.neighbors.add(neighborId);
    }

    public void addNodeNighbor(Node node) {
        this.nodeNeighbors.add(node);
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Integer> getNeighbors () {
        return this.neighbors;
    }

    public ArrayList<Node> getNodeNeighbors () {
        return this.nodeNeighbors;
    }

}

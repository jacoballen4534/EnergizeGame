package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {
    //Holds all the levels,
    private ArrayList<BufferedImage> levels = new ArrayList<>();
    private int currentLevel = 0;
    //May need to put this into a level class so we dont need to get the floor each time.
    private ArrayList<Floor> currentFloors = new ArrayList<>();

    public Map(PreLoadedImages preLoadedImages){
        this.levels.add(preLoadedImages.getTutorialRoom());
    }

    public  BufferedImage getLevel(int levelIndex) {
        if (this.levels.size() >= levelIndex) {
            return this.levels.get(levelIndex);
        } else {
            return null;
        }
    }

    public void addFloor(Floor floor) {
        this.currentFloors.add(floor);
    }

    public ArrayList<Floor> getCurrentFloors() {
        return this.currentFloors;
    }
}

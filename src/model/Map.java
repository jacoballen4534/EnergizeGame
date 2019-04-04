package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {
    //Holds all the levels,
    private ArrayList<BufferedImage> levels = new ArrayList<>();
    private int currentLevel = 0;
    //May need to put this into a level class so we dont need to get the floor each time.
    private ArrayList<Floor> currentFloors = new ArrayList<>();
    private BufferedImage currentLevelImage;

    public Map(PreLoadedImages preLoadedImages){
        this.levels.add(preLoadedImages.getTutorialRoom());
        this.currentLevelImage = preLoadedImages.getTutorialRoom();
    }

    public  BufferedImage getLevel(int levelIndex) {
        if (this.levels.size() >= levelIndex) {
            //By only updating currentLevelImage, this will ensure if a non existent level is requested it will still return a valid level
            this.currentLevelImage = this.levels.get(levelIndex);
            this.currentLevel = levelIndex;
        }
        return currentLevelImage;
    }

    public BufferedImage getCurrentLevel() {
        return this.currentLevelImage;
    }

    public int getCurrentLevelHeight() {
        return this.currentLevelImage.getHeight() * 32; //32 is the height of the floor tile.
    }

    public int getCurrentLevelWidth() {
        return this.currentLevelImage.getWidth() * 32; //32 is the width of the floor tile.
    }

    public void addFloor(Floor floor) {
        this.currentFloors.add(floor);
    }

    public ArrayList<Floor> getCurrentFloors() {
        return this.currentFloors;
    }
}

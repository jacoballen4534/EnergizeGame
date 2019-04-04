package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PreLoadedImages {
    private BufferedImage tutorialRoom = null;
    private BufferedImage wallSpriteSheet = null;
    private BufferedImage doorSpriteSheet = null;
    private BufferedImage floorSpriteSheet = null;
    private BufferedImage gruntSpriteSheet = null;
    private BufferedImage protagonistSpriteSheet = null;


    public PreLoadedImages() {
        try {
            this.tutorialRoom = ImageIO.read(new File(PreLoadedImages.class.getClassLoader().getResource("Images/GameMap.png").getPath()));
//            this.wallSpriteSheet = ImageIO.read(new File(PreLoadedImages.class.getClassLoader().getResource("Images/GameMap.png").getPath()));
//            this.doorSpriteSheet = ImageIO.read(new File(PreLoadedImages.class.getClassLoader().getResource("Images/GameMap.png").getPath()));
            this.floorSpriteSheet = ImageIO.read(new File(PreLoadedImages.class.getClassLoader().getResource("Images/Floor.png").getPath()));
//            this.gruntSpriteSheet = ImageIO.read(new File(PreLoadedImages.class.getClassLoader().getResource("Images/GameMap.png").getPath()));
            this.protagonistSpriteSheet = ImageIO.read(new File(PreLoadedImages.class.getClassLoader().getResource("Images/Protagonist.png").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getTutorialRoom() {
        return this.tutorialRoom;
    }

    public BufferedImage getWallSpriteSheet() {
        return this.wallSpriteSheet;
    }

    public BufferedImage getDoorSpriteSheet() {
        return this.doorSpriteSheet;
    }

    public BufferedImage getFloorSpriteSheet() {
        return this.floorSpriteSheet;
    }

    public BufferedImage getGruntSpriteSheet() {
        return this.gruntSpriteSheet;
    }

    public BufferedImage getProtagonistSpriteSheet() {
        return this.protagonistSpriteSheet;
    }
}

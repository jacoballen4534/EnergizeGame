package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PreLoadedImages {
    private BufferedImage tutorialRoom = null;
    private BufferedImage wallSpriteSheet = null;
    private BufferedImage doorSpriteSheet = null;
    private BufferedImage floorSpriteSheet = null;
    private BufferedImage gruntSpriteSheet = null;
    private BufferedImage protagonistSpriteSheet = null;
    private BufferedImage campFireSpriteSheet = null;


    public PreLoadedImages() {
        try {
            this.tutorialRoom = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/GameMap.png"));
//            this.wallSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Floor.png"));
            this.doorSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Door.png"));
            this.floorSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Floor.png"));
//            this.gruntSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Floor.png"));
            this.protagonistSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/ProtagonistSheet.png"));
            this.campFireSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/CampFire.png"));
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

    public BufferedImage getcampFireSpriteSheet() {
        return this.campFireSpriteSheet;
    }
}

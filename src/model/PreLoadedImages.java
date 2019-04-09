package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PreLoadedImages {
    public static BufferedImage tutorialRoom;
    //    public static BufferedImage wallSpriteSheet;
    public static BufferedImage doorSpriteSheet;
    public static BufferedImage floorSpriteSheet;
    //    public static BufferedImage gruntSpriteSheet;
    public static BufferedImage protagonistSpriteSheet;
    public static BufferedImage campFireSpriteSheet;

    static {
        try {
            tutorialRoom = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/TutorialRoom.png"));
//          wallSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Wall.png"));
            doorSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/DoorCrop.png"));
            floorSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Floor.png"));
//            gruntSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Grunt.png"));
            protagonistSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/ProtagonistSheetx8.png"));
            campFireSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/CampFire.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

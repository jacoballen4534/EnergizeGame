package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class PreLoadedImages {
    //Load all of the images on startup, static and available everywhere.
    public static BufferedImage tutorialRoom;
    public static BufferedImage bossRoom;
    public static BufferedImage doorSpriteSheet;
    public static BufferedImage tileSpriteSheet;
    public static BufferedImage gruntSpriteSheet;
    public static BufferedImage protagonistSpriteSheet;
    public static BufferedImage campFireSpriteSheet;
    public static BufferedImage shieldSpriteSheet;
    public static BufferedImage healthPickupSprite;
    public static BufferedImage energyPickupSprite;
    public static BufferedImage fireScrollSprite;
    public static BufferedImage bossSpriteSheet;
    public static BufferedImage emptyItemSlot;

    static {
        try {
            tutorialRoom = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/TutorialRoomUpdate.png"));
            doorSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Doors.png"));
            tileSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Tileset.png"));
            gruntSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/GruntSpriteSheet.png"));
            protagonistSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/ProtagonistSheet.png"));
            campFireSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/CampFire.png"));
            shieldSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Shield.png"));
            healthPickupSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/healthKit.png"));
            energyPickupSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/energyKit.png"));
            fireScrollSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/fireScroll.png"));
            emptyItemSlot = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/emptyItemSlot.png"));
            bossSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/BossSpriteSheetx3.png"));
            bossRoom = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/BossRoom.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
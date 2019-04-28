package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

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
    public static BufferedImage onlinePlayerSpriteSheet;
    public static BufferedImage campFireSpriteSheet;
    public static BufferedImage shieldSpriteSheet;
    public static BufferedImage healthPickupSprite;
    public static BufferedImage energyPickupSprite;
    public static BufferedImage fireScrollSprite;
    public static BufferedImage iceScrollSprite;
    public static BufferedImage windScrollSprite;
    public static BufferedImage bossSpriteSheet;
    public static BufferedImage emptyItemSlot;
    public static BufferedImage emptyHeart;
    public static BufferedImage fullHeart;
    public static Image mapNoRoom;
    public static Image mapRoomUnVisited;
    public static Image mapRoomVisited;
    public static Image mapCurrentRoom;
    public static Image mapBossEnterance;


    static {
        try {
            tutorialRoom = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/TutorialRoomUpdate.png"));
            doorSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Doors.png"));
            tileSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Tileset.png"));
            gruntSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/GruntSpriteSheet.png"));
            protagonistSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/ProtagonistSheet.png"));
            onlinePlayerSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/BlueCapeProtagonistSheet.png"));
            campFireSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/CampFire.png"));
            shieldSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/Shield.png"));
            healthPickupSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/healthKit.png"));
            energyPickupSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/energyKit.png"));
            fireScrollSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/fireScroll.png"));
            iceScrollSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/iceScroll.png"));
            windScrollSprite = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/windScroll.png"));
            emptyItemSlot = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/emptyItemSlot.png"));
            bossSpriteSheet = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/BossSpriteSheetx3.png"));
            bossRoom = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/Images/BossRoom.png"));
            emptyHeart = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/heartEmpty.png"));
            fullHeart = ImageIO.read(PreLoadedImages.class.getResourceAsStream("/sprites/heartFull.png"));
            mapNoRoom = new Image(PreLoadedImages.class.getResourceAsStream("/sprites/map_noRoom.png"));
            mapRoomUnVisited = new Image(PreLoadedImages.class.getResourceAsStream("/sprites/map_roomUnVisited.png"));
            mapRoomVisited = new Image(PreLoadedImages.class.getResourceAsStream("/sprites/map_roomVisited.png"));
            mapCurrentRoom = new Image(PreLoadedImages.class.getResourceAsStream("/sprites/map_currentRoom.png"));
            mapBossEnterance = new Image(PreLoadedImages.class.getResourceAsStream("/sprites/map_bossEntrance.png"));
        } catch (IOException e) {
            System.out.println("\033[0;31m" + e.getMessage());
        }
    }
}
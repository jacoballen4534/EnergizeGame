package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class BufferImageLoader {

    private BufferedImage image;

    public BufferedImage loadImage(String path) {
        try {
            image = ImageIO.read(new File(BufferImageLoader.class.getClassLoader().getResource("GameMap.png").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}

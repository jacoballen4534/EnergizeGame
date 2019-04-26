package model;

import Multiplayer.Server;

import java.awt.image.BufferedImage;

public class OnlinePlayer extends Protagonist {
    public OnlinePlayer(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
    }


    protected void tick(double cameraX, double cameraY, String commands) {
//        String[] comandTokens = commands.split();
//
//        String temp = commands.split(Server.PACKET_PROTAGONIST_UPDATE + "|" + Server.PACKET_END)[1];
//        int id = Integer.parseInt(temp.split(Server.PACKET_ID + "|" + Server.PACKET_LEVEL_NUMBER)[1]);
//        if (this.id != id) { //Only update the correct online player
//            return;
//        }
//
//        String[] comandTokens = commands.split("/");
//        id =


    }

    protected void tick(double cameraX, double cameraY, KeyInput keyInput) {}

}

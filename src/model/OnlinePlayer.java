package model;

import Multiplayer.Server;

import java.awt.image.BufferedImage;

public class OnlinePlayer extends Protagonist {
    public OnlinePlayer(int x, int y, BufferedImage image, int spriteWidth, int spriteHeight, int renderWidth, int renderHeight, int levelWidth) {
        super(x, y, image, spriteWidth, spriteHeight, renderWidth, renderHeight, levelWidth);
    }


    protected void tick(double cameraX, double cameraY, String commands) {
        if (commands.length() == 0) {
            return;
        }
        String[] temp;//For splitting
        commands = commands.split(Server.PACKET_PROTAGONIST_UPDATE)[1]; //Remove /pu/

        temp = commands.split(Server.PACKET_ID + "|" + Server.PACKET_LEVEL_NUMBER); //Get the player id this packet is for
        int id = Integer.parseInt(temp[1]);
        if (this.id != id) { //Only update the correct online player
            return;
        }
        commands = temp[2]; // /ln/#/pos/
        temp = commands.split(Server.PACKET_POSITION);

        this.levelNumber = Integer.parseInt(temp[0]);


        double newX = Double.parseDouble(temp[1].split(",")[0]);
        double newY = Double.parseDouble(temp[1].split(",")[1]);

        if (commands.contains(Server.PACKET_ATTACK)){
            this.attack();
        }

        if (commands.contains(Server.PACKET_USE_HEALTH)){
            this.inventory.setEquippedItem(new Pickup("Health Kit", Level.HEALTH_KIT_DESCRIPTION, 0,0,
                    PreLoadedImages.healthPickupSprite,Level.PICKUP_SPRITE_WIDTH,Level.PICKUP_SPRITE_HEIGHT));
            useItem();
        }

        if (commands.contains(Server.PACKET_USE_ENERGY)){
            this.inventory.setEquippedItem(new Pickup("Energy Kit", Level.ENERGY_KIT_DESCRIPTION, 0,0,
                    PreLoadedImages.energyPickupSprite,Level.PICKUP_SPRITE_WIDTH,Level.PICKUP_SPRITE_HEIGHT));
            useItem();
        }

        if (commands.contains(Server.PACKET_USE_FIRE)){
            this.inventory.addItem(new Scroll("Fire Scroll", Level.SCROLL_DESCRIPTION, 0, 0,
                    PreLoadedImages.fireScrollSprite, Level.SCROLL_SPRITE_WIDTH, Level.SCROLL_SPRITE_HEIGHT));
            useItem();
        }

        if (commands.contains(Server.PACKET_USE_ICE)){
            this.inventory.addItem(new Scroll("Ice Scroll", Level.SCROLL_DESCRIPTION, 0, 0,
                    PreLoadedImages.iceScrollSprite, Level.SCROLL_SPRITE_WIDTH, Level.SCROLL_SPRITE_HEIGHT));
            useItem();
        }

        if (commands.contains(Server.PACKET_USE_WIND)){
            this.inventory.addItem(new Scroll("Wind Scroll", Level.SCROLL_DESCRIPTION, 0, 0,
                    PreLoadedImages.windScrollSprite, Level.SCROLL_SPRITE_WIDTH, Level.SCROLL_SPRITE_HEIGHT));
            useItem();
        }

        if (commands.contains(Server.PACKET_BLOCK)) {
            this.block();
        }

        if (commands.contains(Server.PACKET_CHEAT)) {
            this.cheat();
        }

        this.velocityX = (float)(newX - this.x);
        this.velocityY = (float)(newY - this.y);
        if (Math.abs((float)(newX - this.x)) > 100 || Math.abs((float)(newY - this.y)) > 100) {
            this.x = newX;
            this.y = newY;
        } else {
            commands = ""; //Set it to empty so the same command doesnt get registered twice
            super.tick(cameraX, cameraY, commands);
        }

    }

    protected void tick(double cameraX, double cameraY, KeyInput keyInput) {}

}

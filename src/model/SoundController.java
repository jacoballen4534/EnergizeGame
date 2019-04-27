package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class SoundController {

    //private static InputStream inputStream;InputStream
    //private static BufferedInputStream bufferedInputStream;
    //private static AudioInputStream audioInput;
    //private static Clip clip;

    private static final HashMap<String,String> audioClips = new HashMap<String,String>(){{
        put("missAttack","/sound/missSwordAttack.wav");
        put("hitAttack","/sound/hitSwordAttack.wav");
        put("fireArrow",null);
        put("arrowHit",null);
        put("getHit",null);
        put("buttonClicked",null);
        put("gameLose", "/sound/gameLose.wav");
        put("bossDeath","/sound/bossDeath.wav"); //Not implemented
        put("gameWin", "/sound/gameWin.wav"); //Not implemented
        put("gruntDeath","/sound/explosion.wav"); //Not final
        put("magicAbility","/sound/magicBlast.wav");
        put("itemPickup","/sound/pickup.wav");
    }};

    public static boolean PlayAudio(String audioName){
        try{
            InputStream inputStream = SoundController.class.getResourceAsStream(audioClips.get(audioName));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(bufferedInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(0);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

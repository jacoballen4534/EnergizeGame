package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class SoundController {

    private static InputStream inputStream;
    private static BufferedInputStream bufferedInputStream;
    private static AudioInputStream audioInput;
    private static Clip clip;

    private static final HashMap<String,String> audioClips = new HashMap<String,String>(){{
        put("missAttack",null);
        put("hitAttack",null);
        put("fireArrow",null);
        put("arrowHit",null);
        put("getHit",null);
        put("buttonClicked",null);
        put("gameOver",null);
        put("bossKill",null);
    }};

    public static boolean PlayAudio(String audioName){
        try{
            inputStream = SoundController.class.getResourceAsStream(audioClips.get(audioName));
            bufferedInputStream = new BufferedInputStream(inputStream);
            audioInput = AudioSystem.getAudioInputStream(bufferedInputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(0);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}

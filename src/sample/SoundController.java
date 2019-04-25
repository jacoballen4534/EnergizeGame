package sample;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class SoundController {

    //Gain control is from -5 to 5
    public static float MASTER_GAIN_CONTROL = 0;
    public static float MUSIC_GAIN_CONTROL = 0;
    public static float SOUNDEFFECTS_GAIN_CONTROL = 0;

    private static final HashMap<String,String> audioSFX = new HashMap<String,String>(){{
        //For Player
        put("missAttackSword","/sound/missSwordAttack.wav");
        put("hitAttackSword","/sound/hitSwordAttack.wav");
        put("fireScroll","/sound/fireExplosion.wav");
        put("iceScroll","/sound/iceFreezing.wav");
        put("windScroll",null);
        put("magicAbility","/sound/magicEffect.wav");
        put("itemPickup","/sound/pickupItem.wav");
        //For enemies
        put("missAttackAxe","/sound/missAxeAttack.wav");
        put("hitAttackAxe","/sound/hitAxeAttack.wav");
        put("gruntDeath",null);
        //For menu
        put("buttonConfirm","/sound/confirm.wav");
        put("buttonCancel","/sound/cancel.wav");
        put("error","/sound/error.wav");
        //For ending the game
        put("gameLose","/sound/gameLose.wav");
        put("gameWin","/sound/gameWin.wav");
    }};

    private static final HashMap<String,String> audioBGM = new HashMap<String, String>(){{
       put("titleBGM","/music/titleScreenBGM.wav");
       put("gameBGM","/music/mainGameBGM.wav");
       put("bossBGM","/music/bossFightBGM.wav");
    }};

    public static Clip playSoundFX(String audioName){
        try{

            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            InputStream inputStream = SoundController.class.getResourceAsStream(audioSFX.get(audioName));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            stream = AudioSystem.getAudioInputStream(bufferedInputStream);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);

            clip.open(stream);
            clip.start();

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-5.0f + SOUNDEFFECTS_GAIN_CONTROL + MASTER_GAIN_CONTROL);

            return clip;

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Clip playMusic(String audioName){
        try{

            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            InputStream inputStream = SoundController.class.getResourceAsStream(audioBGM.get(audioName));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            stream = AudioSystem.getAudioInputStream(bufferedInputStream);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);

            /*System.out.println("=========================");
            System.out.println(clip.isControlSupported(FloatControl.Type.MASTER_GAIN));
            System.out.println("=========================");*/

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.open(stream);
            clip.start();

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-5.0f + MUSIC_GAIN_CONTROL + MASTER_GAIN_CONTROL);

            return clip;

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

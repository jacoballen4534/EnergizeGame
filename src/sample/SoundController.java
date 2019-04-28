package sample;

import javafx.application.Platform;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class SoundController {

    public static float MASTER_GAIN_CONTROL = 1f;
    public static float MUSIC_GAIN_CONTROL = 0.5f;
    public static float SOUNDEFFECTS_GAIN_CONTROL = 0.5f;

    private static final HashMap<String, String> audioSFX = new HashMap<String, String>() {{
        //For Player
        put("missAttackSword", "/sound/missSwordAttack.wav");
        put("hitAttackSword", "/sound/hitSwordAttack.wav");
        put("fireScroll", "/sound/fireExplosion.wav");
        put("iceScroll", "/sound/iceFreezing.wav");
        put("windScroll", null);
        put("magicAbility", "/sound/magicEffect.wav");
        put("itemPickup", "/sound/pickupItem.wav");
        //For enemies
        put("missAttackAxe", "/sound/missAxeAttack.wav");
        put("hitAttackAxe", "/sound/hitAxeAttack.wav");
        put("gruntDeath", null);
        //For menu
        put("buttonConfirm", "/sound/confirm.wav");
        put("buttonCancel", "/sound/cancel.wav");
        put("error", "/sound/error.wav");
        //For ending the game
        put("gameLose", "/sound/gameLose.wav");
        put("gameWin", "/sound/gameWin.wav");
    }};

    private static final HashMap<String, String> audioBGM = new HashMap<String, String>() {{
        put("titleBGM", "/music/titleScreenBGM.wav");
        put("gameBGM", "/music/mainGameBGM.wav");
        put("bossBGM", "/music/bossFightBGM.wav");
    }};

    private static Clip BGM;

    public static Clip playSoundFX(String audioName) {
        try {

            AudioInputStream stream = AudioSystem.getAudioInputStream(SoundController.class.getResourceAsStream(audioSFX.get(audioName)));
            Clip clip = AudioSystem.getClip();
            clip.open(stream);

            //Manage volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float linearVolume = MUSIC_GAIN_CONTROL * MASTER_GAIN_CONTROL;
            float decibelVolume = (float) Math.log10(linearVolume) * 20.0f;
            gainControl.setValue(decibelVolume);

            clip.start();

            return clip;

        } catch (NullPointerException e) {
            System.out.println("Requested sound was unavailable");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Clip playMusic(String audioName) {
        try {

            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;

            InputStream inputStream = SoundController.class.getResourceAsStream(audioBGM.get(audioName));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            stream = AudioSystem.getAudioInputStream(bufferedInputStream);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            BGM = (Clip) AudioSystem.getLine(info);

            BGM.open(stream);
            BGM.start();
            BGM.loop(Clip.LOOP_CONTINUOUSLY);

            updateVolume();

            BGM.addLineListener(lineEvent -> {
                if (!BGM.isRunning()) BGM.start();
            });

            return BGM;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void changeMusic(String audioName) {
        BGM.stop();
        BGM.close();
        playMusic(audioName);
    }

    public static void updateVolume() {

        FloatControl gainControl = (FloatControl) BGM.getControl(FloatControl.Type.MASTER_GAIN);

        float linearVolume = MUSIC_GAIN_CONTROL * MASTER_GAIN_CONTROL;
        float decibelVolume = (float) Math.log10(linearVolume) * 20.0f;

        gainControl.setValue(decibelVolume);
    }
}

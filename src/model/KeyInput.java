package model;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import javax.xml.crypto.dsig.keyinfo.KeyName;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.Map;
import java.util.HashMap;

public class KeyInput{

    private Scene scene;
    private HashMap<KeyCode, Boolean> keyBinds = new HashMap<>();
    private HashMap<String, KeyCode> keyMap = new HashMap<String, KeyCode>(){ //TODO: Read custom keybinds in, convert to keycodes and set
        {
            put("up",KeyCode.W);
            put("left",KeyCode.A);
            put("down",KeyCode.S);
            put("right",KeyCode.D);
            put("attack",KeyCode.J);
            put("useItem",KeyCode.K);
            put("block",KeyCode.L);
            put("useSpecial",KeyCode.U);
            put("inventory", KeyCode.I);
            put("cheatKey",KeyCode.C);
            put("jump",KeyCode.SPACE);
            put("pause",KeyCode.P);
            put("quit",KeyCode.ESCAPE);
        }
    };

    public KeyInput(Scene scene) {
        this.scene = scene;

        for (Object key: keyMap.keySet()){
            keyBinds.put(keyMap.get(key),false);
        }

        this.scene.setOnKeyPressed(keyEvent -> {
            if (keyBinds.containsKey(keyEvent.getCode())) { //One of the correct keys are pressed
                keyBinds.put(keyEvent.getCode(), true);
            }
        });

        this.scene.setOnKeyReleased(keyEvent -> {
            if (keyBinds.containsKey(keyEvent.getCode())) { //One of the correct keys are released
                keyBinds.put(keyEvent.getCode(), false);
                //keyBinds.remove(keyEvent.getCode());
            }
        });
    }

    public boolean getKeyPressed(String keyName){
        if (keyMap.containsKey(keyName) && keyBinds.containsKey(keyMap.get(keyName))) { //Check that they action has a key associated with it.
            return keyBinds.get(keyMap.get(keyName));
        } else {
            return false;
        }
    }

    //From here: https://stackoverflow.com/questions/37472273/detect-single-key-press-in-javafx
    public boolean getKeyPressDebounced(String keyName){
        Boolean isActive = keyBinds.get(keyMap.get(keyName));

        if (isActive != null && isActive) {
            keyBinds.put(keyMap.get(keyName), false);
            return true;
        } else {
            return false;
        }
    }
}

package model;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class KeyInput{

    private Scene scene;
    private HashMap<KeyCode, Boolean> keyBinds = new HashMap<>();
    private HashMap<String, KeyCode> keyMap = new HashMap<String, KeyCode>(){
        {
            put("up",KeyCode.W);
            put("left",KeyCode.A);
            put("down",KeyCode.S);
            put("right",KeyCode.D);
            put("attack",KeyCode.J);
            put("useItem",KeyCode.K);
            put("useSpecial",KeyCode.L);
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

    public boolean getKeyPress(String keyName){
        return keyBinds.get(keyMap.get(keyName));
    }

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

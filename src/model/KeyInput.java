package model;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.Map;

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
            }
        });
    }

    public boolean getKeyPressed(String keyName){
        return keyBinds.get(keyMap.get(keyName));
    }
}
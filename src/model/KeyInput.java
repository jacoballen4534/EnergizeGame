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

    //Need a dictionary or something to connect the actions with their key

    //TODO: Can put this in the handler instead of protagonist
    private Scene scene;
    public HashMap<KeyCode, Boolean> keyBinds = new HashMap<>();
    public Map<String, KeyCode> keyMap = new HashMap<>(){
        {
            put("up",KeyCode.W);
            put("left",KeyCode.A);
            put("down",KeyCode.S);
            put("right",KeyCode.D);
            put("attack",KeyCode.J);
            put("useItem",KeyCode.K);
            put("useSpecial",KeyCode.L);
            put("jump",KeyCode.SPACE);
            put("pause",KeyCode.P);
            put("quit",KeyCode.ESCAPE);
        }
    };

    // Using this to make it easier to custom add key bindings later
    public boolean up = false, down = false,
            left = false, right = false,
            attack = false, useItem = false,
            useSpecial = false, jump = false,
            pause = false, quit = false;

    public KeyInput(Scene scene) {
        this.scene = scene;

        for (Object key: keyMap.keySet()){
            keyBinds.put(keyMap.get(key),false);
        }

        this.scene.setOnKeyPressed(keyEvent -> {
            if (keyBinds.containsKey(keyEvent.getCode())) { //One of the correct keys are pressed
                keyBinds.put(keyEvent.getCode(), true);
                updateActions();
            }
        });

        this.scene.setOnKeyReleased(keyEvent -> {
            if (keyBinds.containsKey(keyEvent.getCode())) { //One of the correct keys are pressed
                keyBinds.put(keyEvent.getCode(), false);
                updateActions();
            }
        });
    }

    private void updateActions() {
        this.up = keyBinds.get(KeyCode.W);
        this.down = keyBinds.get(KeyCode.S);
        this.left = keyBinds.get(KeyCode.A);
        this.right = keyBinds.get(KeyCode.D);
        this.jump = keyBinds.get(KeyCode.SPACE);
        this.attack = keyBinds.get(KeyCode.J);
        this.useItem = keyBinds.get(KeyCode.K);
        this.useSpecial = keyBinds.get(KeyCode.L);
        this.pause = keyBinds.get(KeyCode.P);
        this.quit = keyBinds.get(KeyCode.ESCAPE);
    }

    public boolean getKeyPressed(String keyName){
        return keyBinds.get(keyMap.get(keyName));
    }
}
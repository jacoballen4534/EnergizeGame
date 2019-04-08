package model;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

public class KeyInput{

    //TODO: Can put this in the handler instead of protagonist
    private Scene scene;
    public HashMap<KeyCode, Boolean> keyBinds = new HashMap<>();

    // Using this to make it easier to custom add key bindings later
    public boolean up = false, down = false, left = false, right = false;

    public KeyInput(Scene scene) {
        this.scene = scene;
        keyBinds.put(KeyCode.W, false);//up
        keyBinds.put(KeyCode.A, false);//left
        keyBinds.put(KeyCode.S, false);//down
        keyBinds.put(KeyCode.D, false);//right

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
    }
}

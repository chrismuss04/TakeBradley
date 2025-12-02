package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.entity.Player;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Player player;
    private final Set<KeyCode> pressedKeys;
    
    public InputHandler(Player player) {
        this.player = player;
        this.pressedKeys = new HashSet<>();
    }
    
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        
        // Avoid repeat events
        if (pressedKeys.contains(code)) {
            return;
        }
        
        pressedKeys.add(code);
        
        // Inventory slot selection - 1-5
        if (code == KeyCode.DIGIT1) {
            player.equipItem(0);
        } else if (code == KeyCode.DIGIT2) {
            player.equipItem(1);
        } else if (code == KeyCode.DIGIT3) {
            player.equipItem(2);
        } else if (code == KeyCode.DIGIT4) {
            player.equipItem(3);
        } else if (code == KeyCode.DIGIT5) {
            player.equipItem(4);
        }
        
        // Combat - Space to shoot
        else if (code == KeyCode.SPACE) {
            player.fireWeapon();
        }
        
        // Reload - R
        else if (code == KeyCode.R) {
            player.reload();
        }
        
        // Use item - E (for medical items)
        else if (code == KeyCode.E) {
            if (player.getEquippedItem() != null) {
                player.getEquippedItem().use(player);
            }
        }
    }
    
    public void handleKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        pressedKeys.remove(code);
    }
    
    // Called each frame to handle continuous movement
    public void update(double deltaTime) {
        // Handle movement based on currently pressed keys
        // move() expects direction (-1, 0, 1) and multiplies by speed internally
        if (pressedKeys.contains(KeyCode.W) || pressedKeys.contains(KeyCode.UP)) {
            player.setFacingDirection(Direction.UP);
            player.move(0, -1);
        } else if (pressedKeys.contains(KeyCode.S) || pressedKeys.contains(KeyCode.DOWN)) {
            player.setFacingDirection(Direction.DOWN);
            player.move(0, 1);
        }
        
        if (pressedKeys.contains(KeyCode.A) || pressedKeys.contains(KeyCode.LEFT)) {
            player.setFacingDirection(Direction.LEFT);
            player.move(-1, 0);
        } else if (pressedKeys.contains(KeyCode.D) || pressedKeys.contains(KeyCode.RIGHT)) {
            player.setFacingDirection(Direction.RIGHT);
            player.move(1, 0);
        }
    }
}

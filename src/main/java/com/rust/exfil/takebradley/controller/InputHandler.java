package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.gear.GearItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Player player;
    private final GameWorld gameWorld;
    private final Set<KeyCode> pressedKeys;
    
    public InputHandler(Player player, GameWorld gameWorld) {
        this.player = player;
        this.gameWorld = gameWorld;
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
        } else if (code == KeyCode.DIGIT6) {
            player.equipItem(5);
        } else if (code == KeyCode.DIGIT7) {
            player.equipItem(6);
        } else if (code == KeyCode.DIGIT8) {
            player.equipItem(7);
        } else if (code == KeyCode.DIGIT9) {
            player.equipItem(8);
        } else if (code == KeyCode.DIGIT0) {
            player.equipItem(9);
        }

        
        // Combat - Space to shoot
        else if (code == KeyCode.SPACE) {
            if(player.getEquippedItem() instanceof WeaponItem) {
                player.fireWeapon();
            }
        }
        
        // Reload - R
        else if (code == KeyCode.R) {
            if(player.getEquippedItem() instanceof WeaponItem) {
                player.reload();
            }
        }
        
        // Use item - E (for medical items and gear)
        else if (code == KeyCode.E) {
            LootItem equippedItem = player.getEquippedItem();
            if (equippedItem != null) {
                // Special handling for gear to avoid duplication bug
                if (equippedItem instanceof GearItem) {
                    player.getInventory().equipGearFromSlot(player.getSelectedSlotIndex());
                } else {
                    // For other items (medical, etc.), use normally
                    equippedItem.use(player);
                }
            }
        }
        
        // loot container - F
        else if (code == KeyCode.F) {
            // find nearest container within interaction range (50 pixels)
            Entity nearestContainer = 
                gameWorld.findNearestContainer(player, 50.0);
            
            if (nearestContainer != null) {
                // loot all items from container
                gameWorld.lootContainer(player, nearestContainer);
            }
        }
    }
    
    public void handleKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        pressedKeys.remove(code);
    }
    
    // Called each frame to set movement intent based on pressed keys
    public void update(double deltaTime) {
        // Set movement intent based on currently pressed keys
        // The actual movement will be applied in Player.update() which is called by GameWorld.updateAll()
        double dx = 0;
        double dy = 0;
        
        if (pressedKeys.contains(KeyCode.W) || pressedKeys.contains(KeyCode.UP)) {
            player.setFacingDirection(Direction.UP);
            dy = -1;
        } else if (pressedKeys.contains(KeyCode.S) || pressedKeys.contains(KeyCode.DOWN)) {
            player.setFacingDirection(Direction.DOWN);
            dy = 1;
        }
        
        if (pressedKeys.contains(KeyCode.A) || pressedKeys.contains(KeyCode.LEFT)) {
            player.setFacingDirection(Direction.LEFT);
            dx = -1;
        } else if (pressedKeys.contains(KeyCode.D) || pressedKeys.contains(KeyCode.RIGHT)) {
            player.setFacingDirection(Direction.RIGHT);
            dx = 1;
        }
        
        // Set the movement intent - actual movement happens in Player.update()
        player.setMovementIntent(dx, dy);
    }
}

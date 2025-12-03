package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.gear.GearItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import com.rust.exfil.takebradley.view.LootUIRenderer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Player player;
    private final GameWorld gameWorld;
    private final ExfilController exfilController;
    private final Set<KeyCode> pressedKeys;
    private LootUIRenderer lootUIRenderer;
    
    // Extraction state
    private boolean isExtracting = false;
    private double extractionProgress = 0.0;
    private static final double EXTRACTION_TIME = 10.0; // 10 seconds
    
    public InputHandler(Player player, GameWorld gameWorld, ExfilController exfilController) {
        this.player = player;
        this.gameWorld = gameWorld;
        this.exfilController = exfilController;
        this.pressedKeys = new HashSet<>();
    }
    
    public boolean isExtracting() {
        return isExtracting;
    }
    
    public double getExtractionProgress() {
        return extractionProgress / EXTRACTION_TIME; // Returns 0.0 to 1.0
    }
    
    public void setLootUIRenderer(LootUIRenderer lootUIRenderer) {
        this.lootUIRenderer = lootUIRenderer;
    }
    
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        
        // Avoid repeat events
        if (pressedKeys.contains(code)) {
            return;
        }
        
        pressedKeys.add(code);
        
        // Handle loot UI input if open
        if (lootUIRenderer != null && lootUIRenderer.isOpen()) {
            handleLootUIInput(code);
            return; // Don't process game input while loot UI is open
        }
        
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
        
        // loot container - F (open loot UI)
        else if (code == KeyCode.F) {
            // find nearest container within interaction range (50 pixels)
            Entity nearestContainer = 
                gameWorld.findNearestContainer(player, 50.0);
            
            if (nearestContainer != null && lootUIRenderer != null) {
                // Open loot UI instead of auto-looting
                lootUIRenderer.openLootUI(nearestContainer, 800, 600); // TODO: Get canvas size dynamically
            }
        }
    }
    
    /**
     * Handle input when loot UI is open
     */
    private void handleLootUIInput(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            // Close loot UI
            lootUIRenderer.closeLootUI();
        } else if (code == KeyCode.W || code == KeyCode.UP) {
            // Select item above
            lootUIRenderer.selectUp();
        } else if (code == KeyCode.S || code == KeyCode.DOWN) {
            // Select item below
            lootUIRenderer.selectDown();
        } else if (code == KeyCode.A || code == KeyCode.LEFT) {
            // Select item to the left
            lootUIRenderer.selectLeft();
        } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
            // Select item to the right
            lootUIRenderer.selectRight();
        } else if (code == KeyCode.E) {
            // Take selected item
            Entity container = lootUIRenderer.getCurrentContainer();
            int slotIndex = lootUIRenderer.getSelectedSlotIndex();
            
            if (container != null) {
                boolean success = gameWorld.lootItem(player, container, slotIndex);
                
                if (success) {
                    // Move to next item or close if empty
                    if (container.getInventory().isEmpty()) {
                        lootUIRenderer.closeLootUI();
                    } else {
                        lootUIRenderer.selectRight();
                    }
                }
            }
        } else if (code == KeyCode.F) {
            // Take all items
            Entity container = lootUIRenderer.getCurrentContainer();
            if (container != null) {
                gameWorld.lootContainer(player, container);
                lootUIRenderer.closeLootUI();
            }
        }
    }
    
    public void handleKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        pressedKeys.remove(code);
    }
    
    // Called each frame to set movement intent based on pressed keys
    public void update(double deltaTime) {
        // Don't process movement if loot UI is open
        if (lootUIRenderer != null && lootUIRenderer.isOpen()) {
            player.setMovementIntent(0, 0);
            return;
        }
        
        // Handle full-auto firing if space is held down
        if (pressedKeys.contains(KeyCode.SPACE)) {
            LootItem equippedItem = player.getEquippedItem();
            if (equippedItem instanceof WeaponItem) {
                WeaponItem weapon = (WeaponItem) equippedItem;
                // For full-auto weapons, fire continuously while space is held
                if (weapon.isFullAuto()) {
                    player.fireWeapon();
                }
                // For semi-auto, firing is handled in handleKeyPressed (one shot per press)
            }
        }
        
        // Handle extraction progress
        if (pressedKeys.contains(KeyCode.X)) {
            // Check if player can extract (in zone and alive)
            if (exfilController.canExtract(player)) {
                if (!isExtracting) {
                    // Start extraction
                    isExtracting = true;
                    extractionProgress = 0.0;
                }
                
                // Increment extraction progress
                extractionProgress += deltaTime;
                
                // Check if extraction is complete
                if (extractionProgress >= EXTRACTION_TIME) {
                    // Complete extraction
                    exfilController.extract(player);
                    isExtracting = false;
                    extractionProgress = 0.0;
                }
            } else {
                // Cancel extraction if player left zone or died
                if (isExtracting) {
                    isExtracting = false;
                    extractionProgress = 0.0;
                }
            }
        } else {
            // Cancel extraction if player released X key
            if (isExtracting) {
                isExtracting = false;
                extractionProgress = 0.0;
            }
        }
        
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

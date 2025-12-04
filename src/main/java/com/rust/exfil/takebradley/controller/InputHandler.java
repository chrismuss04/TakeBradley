package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.gear.GearItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import com.rust.exfil.takebradley.view.GameRenderer;
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
    private GameRenderer gameRenderer;
    private Runnable onReturnToStart; 
    
    // extraction state
    private boolean isExtracting = false;
    private double extractionProgress = 0.0;
    private static final double EXTRACTION_TIME = 10.0;
    
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
        return extractionProgress / EXTRACTION_TIME;
    }
    
    public void setLootUIRenderer(LootUIRenderer lootUIRenderer) {
        this.lootUIRenderer = lootUIRenderer;
    }
    
    public void setGameRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }
    
    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }
    
    public void setOnReturnToStart(Runnable callback) {
        this.onReturnToStart = callback;
    }
    
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        
        GameRenderer renderer = getGameRenderer();
        if (renderer != null && renderer.isShowingPostRaid()) {
            // enter key to return to start screen
            if (code == KeyCode.ENTER && onReturnToStart != null) {
                onReturnToStart.run();
            }
            return;
        }
        
        // avoid repeat events
        if (pressedKeys.contains(code)) {
            return;
        }
        
        pressedKeys.add(code);
        
        // handle loot UI input if open
        if (lootUIRenderer != null && lootUIRenderer.isOpen()) {
            handleLootUIInput(code);
            return; // don't process game input while loot UI is open
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

        // combat controls
        // space to shoot
        else if (code == KeyCode.SPACE) {
            if(player.getEquippedItem() instanceof WeaponItem) {
                player.fireWeapon();
            }
        }
        
        // r to reload
        else if (code == KeyCode.R) {
            if(player.getEquippedItem() instanceof WeaponItem) {
                player.reload();
            }
        }
        
        // e to use med item or equip gear
        else if (code == KeyCode.E) {
            LootItem equippedItem = player.getEquippedItem();
            if (equippedItem != null) {
                if (equippedItem instanceof GearItem) {
                    player.getInventory().equipGearFromSlot(player.getSelectedSlotIndex());
                } else {
                    // for other items use normally
                    equippedItem.use(player);
                }
            }
        }
        
        // f to open loot container
        else if (code == KeyCode.F) {
            // find nearest container within interaction range (50 pixels)
            Entity nearestContainer = 
                gameWorld.findNearestContainer(player, 50.0);
            
            if (nearestContainer != null && lootUIRenderer != null) {
                // open loot UI instead of auto-looting
                lootUIRenderer.openLootUI(nearestContainer, 800, 600);
            }
        }
    }
    
    private void handleLootUIInput(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            // close loot UI
            lootUIRenderer.closeLootUI();
        } else if (code == KeyCode.W || code == KeyCode.UP) {
            lootUIRenderer.selectUp();
        } else if (code == KeyCode.S || code == KeyCode.DOWN) {
            lootUIRenderer.selectDown();
        } else if (code == KeyCode.A || code == KeyCode.LEFT) {
            lootUIRenderer.selectLeft();
        } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
            lootUIRenderer.selectRight();
        } else if (code == KeyCode.E) {
            // take selected item
            Entity container = lootUIRenderer.getCurrentContainer();
            int slotIndex = lootUIRenderer.getSelectedSlotIndex();
            
            if (container != null) {
                boolean success = gameWorld.lootItem(player, container, slotIndex);
                
                if (success) {
                    // move to next item or close if empty
                    if (container.getInventory().isEmpty()) {
                        lootUIRenderer.closeLootUI();
                    } else {
                        lootUIRenderer.selectRight();
                    }
                }
            }
        } else if (code == KeyCode.F) {
            // take all items
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
    
    // set movement intent based on keypress
    public void update(double deltaTime) {
        if (lootUIRenderer != null && lootUIRenderer.isOpen()) {
            player.setMovementIntent(0, 0);
            return;
        }
        
        // handle full-auto firing if space is held down
        if (pressedKeys.contains(KeyCode.SPACE)) {
            LootItem equippedItem = player.getEquippedItem();
            if (equippedItem instanceof WeaponItem) {
                WeaponItem weapon = (WeaponItem) equippedItem;
                if (weapon.isFullAuto()) {
                    player.fireWeapon();
                }
            }
        }
        
        // handle extraction progress
        if (pressedKeys.contains(KeyCode.X)) {
            // check if player can extract
            if (exfilController.canExtract(player)) {
                if (!isExtracting) {
                    isExtracting = true;
                    extractionProgress = 0.0;
                }
                
                // increment extraction progress
                extractionProgress += deltaTime;
                
                // check if extraction is complete
                if (extractionProgress >= EXTRACTION_TIME) {
                    exfilController.extract(player);
                    isExtracting = false;
                    extractionProgress = 0.0;
                }
            } else {
                // cancel extraction if player left zone or died
                if (isExtracting) {
                    isExtracting = false;
                    extractionProgress = 0.0;
                }
            }
        } else {
            // cancel extraction if player released x d
            if (isExtracting) {
                isExtracting = false;
                extractionProgress = 0.0;
            }
        }
        
        // set movement intent based on currently pressed keys
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
        
        // set the movement intent for use in Player.update()
        player.setMovementIntent(dx, dy);
    }
}

package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.medical.MedicalItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Renders the loot UI overlay when player is looting a container
 */
public class LootUIRenderer {
    
    // UI layout constants
    private static final double UI_WIDTH = 600;
    private static final double UI_HEIGHT = 450;
    private static final double SLOT_SIZE = 50;
    private static final double SLOT_SPACING = 5;
    private static final double PADDING = 20;
    private static final int SLOTS_PER_ROW = 5;
    
    private static final Font TITLE_FONT = new Font("Arial", 18);
    private static final Font ITEM_FONT = new Font("Arial", 10);
    private static final Font BUTTON_FONT = new Font("Arial", 14);
    
    private Entity currentContainer;
    private int selectedSlotIndex = 0;
    private double uiX, uiY;
    
    /**
     * Open the loot UI for a container
     */
    public void openLootUI(Entity container, double canvasWidth, double canvasHeight) {
        this.currentContainer = container;
        this.selectedSlotIndex = 0;
        
        // Center the UI on screen
        this.uiX = (canvasWidth - UI_WIDTH) / 2;
        this.uiY = (canvasHeight - UI_HEIGHT) / 2;
    }
    
    /**
     * Close the loot UI
     */
    public void closeLootUI() {
        this.currentContainer = null;
        this.selectedSlotIndex = 0;
    }
    
    /**
     * Check if loot UI is currently open
     */
    public boolean isOpen() {
        return currentContainer != null;
    }
    
    /**
     * Get the currently open container
     */
    public Entity getCurrentContainer() {
        return currentContainer;
    }
    
    /**
     * Get the selected slot index
     */
    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }
    
    /**
     * Move selection up (previous row)
     */
    public void selectUp() {
        if (currentContainer == null) return;
        
        Inventory inv = currentContainer.getInventory();
        if (inv == null) return;
        
        // Move up one row
        int targetIndex = selectedSlotIndex - SLOTS_PER_ROW;
        if (targetIndex < 0) {
            // Wrap to bottom
            int rows = (inv.getSize() + SLOTS_PER_ROW - 1) / SLOTS_PER_ROW;
            targetIndex = selectedSlotIndex + (rows - 1) * SLOTS_PER_ROW;
            if (targetIndex >= inv.getSize()) {
                targetIndex -= SLOTS_PER_ROW;
            }
        }
        
        // Find nearest non-empty slot
        selectedSlotIndex = findNearestNonEmptySlot(inv, targetIndex);
    }
    
    /**
     * Move selection down (next row)
     */
    public void selectDown() {
        if (currentContainer == null) return;
        
        Inventory inv = currentContainer.getInventory();
        if (inv == null) return;
        
        // Move down one row
        int targetIndex = selectedSlotIndex + SLOTS_PER_ROW;
        if (targetIndex >= inv.getSize()) {
            // Wrap to top
            targetIndex = selectedSlotIndex % SLOTS_PER_ROW;
        }
        
        // Find nearest non-empty slot
        selectedSlotIndex = findNearestNonEmptySlot(inv, targetIndex);
    }
    
    /**
     * Move selection left
     */
    public void selectLeft() {
        if (currentContainer == null) return;
        
        Inventory inv = currentContainer.getInventory();
        if (inv == null) return;
        
        // Move left one slot
        int targetIndex = selectedSlotIndex - 1;
        if (targetIndex < 0) {
            targetIndex = inv.getSize() - 1;
        }
        
        // Find nearest non-empty slot
        selectedSlotIndex = findNearestNonEmptySlot(inv, targetIndex);
    }
    
    /**
     * Move selection right
     */
    public void selectRight() {
        if (currentContainer == null) return;
        
        Inventory inv = currentContainer.getInventory();
        if (inv == null) return;
        
        // Move right one slot
        int targetIndex = selectedSlotIndex + 1;
        if (targetIndex >= inv.getSize()) {
            targetIndex = 0;
        }
        
        // Find nearest non-empty slot
        selectedSlotIndex = findNearestNonEmptySlot(inv, targetIndex);
    }
    
    /**
     * Find the nearest non-empty slot starting from targetIndex
     */
    private int findNearestNonEmptySlot(Inventory inv, int targetIndex) {
        int startIndex = targetIndex;
        
        // Try forward search first
        int index = targetIndex;
        do {
            if (inv.getItem(index) != null) {
                return index;
            }
            index++;
            if (index >= inv.getSize()) {
                index = 0;
            }
        } while (index != startIndex);
        
        // If no non-empty slot found, return original selection
        return selectedSlotIndex;
    }
    
    /**
     * Render the loot UI overlay
     */
    public void renderLootUI(GraphicsContext gc) {
        if (currentContainer == null) return;
        
        Inventory containerInventory = currentContainer.getInventory();
        if (containerInventory == null) return;
        
        // Draw semi-transparent background overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        // Draw UI panel background
        gc.setFill(Color.rgb(40, 40, 45));
        gc.fillRect(uiX, uiY, UI_WIDTH, UI_HEIGHT);
        
        // Draw UI panel border
        gc.setStroke(Color.rgb(100, 100, 100));
        gc.setLineWidth(2);
        gc.strokeRect(uiX, uiY, UI_WIDTH, UI_HEIGHT);
        
        // Draw title
        gc.setFill(Color.WHITE);
        gc.setFont(TITLE_FONT);
        String title = currentContainer.getName();
        gc.fillText(title, uiX + PADDING, uiY + PADDING + 15);
        
        // Draw instructions
        gc.setFont(ITEM_FONT);
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText("Use WASD or Arrow Keys to navigate, E to take item, ESC to close", 
                    uiX + PADDING, uiY + PADDING + 40);
        
        // Draw container items in grid (like hotbar)
        double gridStartY = uiY + PADDING + 60;
        double gridStartX = uiX + PADDING;
        
        for (int i = 0; i < containerInventory.getSize(); i++) {
            LootItem item = containerInventory.getItem(i);
            
            // Calculate grid position
            int row = i / SLOTS_PER_ROW;
            int col = i % SLOTS_PER_ROW;
            
            double slotX = gridStartX + col * (SLOT_SIZE + SLOT_SPACING);
            double slotY = gridStartY + row * (SLOT_SIZE + SLOT_SPACING);
            
            // Check if this slot is visible
            if (slotY + SLOT_SIZE > uiY + UI_HEIGHT - PADDING - 50) {
                break; // Don't render items outside UI bounds
            }
            
            // Draw slot background
            if (i == selectedSlotIndex && item != null) {
                // Highlight selected slot (only if it has an item)
                gc.setFill(Color.rgb(100, 100, 150));
            } else {
                gc.setFill(Color.rgb(50, 50, 50));
            }
            gc.fillRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);
            
            // Draw slot border
            if (i == selectedSlotIndex && item != null) {
                gc.setStroke(Color.YELLOW);
                gc.setLineWidth(3);
            } else {
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(1);
            }
            gc.strokeRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);
            
            // Draw item in slot if present
            if (item != null) {
                gc.setFill(Color.WHITE);
                gc.setFont(ITEM_FONT);
                
                // Draw item name (abbreviated)
                String itemName = item.getName();
                if (itemName.length() > 8) {
                    itemName = itemName.substring(0, 8);
                }
                gc.fillText(itemName, slotX + 5, slotY + 25);
                
                // Draw quantity/ammo info below name
                String quantityText = "";
                if (item instanceof AmmoItem) {
                    AmmoItem ammo = (AmmoItem) item;
                    quantityText = "x" + ammo.getQuantity();
                } else if (item instanceof WeaponItem) {
                    WeaponItem weapon = (WeaponItem) item;
                    quantityText = weapon.getCurrentAmmo() + "/" + weapon.getMagazineSize();
                } else if (item instanceof MedicalItem) {
                    MedicalItem med = (MedicalItem) item;
                    quantityText = "x" + med.getQuantity();
                }
                
                if (!quantityText.isEmpty()) {
                    gc.fillText(quantityText, slotX + 5, slotY + 40);
                }
            } else {
                // Draw empty slot indicator
                gc.setFill(Color.GRAY);
                gc.setFont(ITEM_FONT);
                gc.fillText("-", slotX + 20, slotY + 30);
            }
        }
        
        // Draw "Take All" button at bottom
        double buttonY = uiY + UI_HEIGHT - PADDING - 40;
        double buttonWidth = 120;
        double buttonHeight = 35;
        double buttonX = uiX + UI_WIDTH - PADDING - buttonWidth;
        
        gc.setFill(Color.rgb(60, 120, 60));
        gc.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        gc.setStroke(Color.rgb(100, 180, 100));
        gc.setLineWidth(2);
        gc.strokeRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        gc.setFill(Color.WHITE);
        gc.setFont(BUTTON_FONT);
        gc.fillText("Take All (F)", buttonX + 10, buttonY + 22);
        
        // Draw "Close" button
        double closeButtonX = uiX + PADDING;
        gc.setFill(Color.rgb(120, 60, 60));
        gc.fillRect(closeButtonX, buttonY, buttonWidth, buttonHeight);
        gc.setStroke(Color.rgb(180, 100, 100));
        gc.setLineWidth(2);
        gc.strokeRect(closeButtonX, buttonY, buttonWidth, buttonHeight);
        
        gc.setFill(Color.WHITE);
        gc.fillText("Close (ESC)", closeButtonX + 10, buttonY + 22);
    }
}

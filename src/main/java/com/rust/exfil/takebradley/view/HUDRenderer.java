package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HUDRenderer {
    
    // HUD layout constants
    private static final double HEALTH_BAR_X = 10;
    private static final double HEALTH_BAR_Y = 10;
    private static final double HEALTH_BAR_WIDTH = 200;
    private static final double HEALTH_BAR_HEIGHT = 20;
    
    private static final double AMMO_X = 10;
    private static final double AMMO_Y = 550;
    
    private static final double HOTBAR_X = 250;
    private static final double HOTBAR_Y = 540;
    private static final double HOTBAR_SLOT_SIZE = 50;
    private static final double HOTBAR_SLOT_SPACING = 5;
    
    private static final Font HUD_FONT = new Font("Arial", 14);
    private static final Font HOTBAR_FONT = new Font("Arial", 10);
    
    // render all hud elements
    public void renderHUD(GraphicsContext gc, Player player) {
        renderHealthBar(gc, player.getHealth(), player.getMaxHealth());
        renderAmmoCounter(gc, player);
        renderHotbar(gc, player);
    }
    
    // render all hud elements with extraction progress
    public void renderHUD(GraphicsContext gc, Player player, boolean isExtracting, double extractionProgress) {
        renderHealthBar(gc, player.getHealth(), player.getMaxHealth());
        
        // render extraction progress bar if extracting
        if (isExtracting) {
            renderExtractionBar(gc, extractionProgress);
        }
        
        renderAmmoCounter(gc, player);
        renderHotbar(gc, player);
    }
    
    // render extraction progress bar below health bar
    private void renderExtractionBar(GraphicsContext gc, double progress) {
        double extractionBarX = HEALTH_BAR_X;
        double extractionBarY = HEALTH_BAR_Y + HEALTH_BAR_HEIGHT + 10; // 10px below health bar
        double extractionBarWidth = HEALTH_BAR_WIDTH;
        double extractionBarHeight = 20;
        
        // draw background
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(extractionBarX, extractionBarY, extractionBarWidth, extractionBarHeight);
        
        // draw progress fill (cyan/blue color for extraction)
        double fillWidth = extractionBarWidth * progress;
        gc.setFill(Color.rgb(0, 200, 255)); // Cyan
        gc.fillRect(extractionBarX, extractionBarY, fillWidth, extractionBarHeight);
        
        // draw border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(extractionBarX, extractionBarY, extractionBarWidth, extractionBarHeight);
        
        // draw extraction text
        gc.setFill(Color.WHITE);
        gc.setFont(HUD_FONT);
        String extractionText = "EXTRACTING... " + String.format("%.1f", progress * 100) + "%";
        gc.fillText(extractionText, extractionBarX + 5, extractionBarY + 15);
    }
    
    // render health bar in top left
    private void renderHealthBar(GraphicsContext gc, int health, int maxHealth) {
        // draw background (empty health bar)
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(HEALTH_BAR_X, HEALTH_BAR_Y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        
        // draw health fill
        double healthPercentage = (double) health / maxHealth;
        double fillWidth = HEALTH_BAR_WIDTH * healthPercentage;
        
        // color based on health percentage
        Color healthColor;
        if (healthPercentage > 0.6) {
            healthColor = Color.rgb(0, 200, 0); // Green
        } else if (healthPercentage > 0.3) {
            healthColor = Color.rgb(255, 200, 0); // Yellow
        } else {
            healthColor = Color.rgb(200, 0, 0); // Red
        }
        
        gc.setFill(healthColor);
        gc.fillRect(HEALTH_BAR_X, HEALTH_BAR_Y, fillWidth, HEALTH_BAR_HEIGHT);
        
        // draw border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(HEALTH_BAR_X, HEALTH_BAR_Y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        
        // draw health text
        gc.setFill(Color.WHITE);
        gc.setFont(HUD_FONT);
        String healthText = health + " / " + maxHealth;
        gc.fillText(healthText, HEALTH_BAR_X + 5, HEALTH_BAR_Y + 15);
    }
    
    // render ammo counter in bottom left
    private void renderAmmoCounter(GraphicsContext gc, Player player) {
        LootItem equippedItem = player.getEquippedItem();
        
        // only display ammo if weapon is equipped
        if (equippedItem instanceof WeaponItem) {
            WeaponItem weapon = (WeaponItem) equippedItem;
            
            gc.setFill(Color.WHITE);
            gc.setFont(HUD_FONT);
            
            String ammoText = "Ammo: " + weapon.getCurrentAmmo() + " / " + weapon.getMagazineSize();
            gc.fillText(ammoText, AMMO_X, AMMO_Y);
            
            // display weapon name
            gc.fillText(weapon.getName(), AMMO_X, AMMO_Y - 20);
        }
    }
    
    // render inventory hotbar in bottom center
    private void renderHotbar(GraphicsContext gc, Player player) {
        int inventorySize = player.getInventory().getSize();
        int selectedSlot = player.getSelectedSlotIndex();
        
        for (int i = 0; i < inventorySize; i++) {
            double slotX = HOTBAR_X + i * (HOTBAR_SLOT_SIZE + HOTBAR_SLOT_SPACING);
            double slotY = HOTBAR_Y;
            
            // draw slot background
            if (i == selectedSlot) {
                // highlight selected slot
                gc.setFill(Color.rgb(100, 100, 150));
            } else {
                gc.setFill(Color.rgb(50, 50, 50));
            }
            gc.fillRect(slotX, slotY, HOTBAR_SLOT_SIZE, HOTBAR_SLOT_SIZE);
            
            // draw slot border
            if (i == selectedSlot) {
                gc.setStroke(Color.YELLOW);
                gc.setLineWidth(3);
            } else {
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(1);
            }
            gc.strokeRect(slotX, slotY, HOTBAR_SLOT_SIZE, HOTBAR_SLOT_SIZE);
            
            // draw item in slot if present
            LootItem item = player.getInventory().getItem(i);
            if (item != null) {
                gc.setFill(Color.WHITE);
                gc.setFont(HOTBAR_FONT);
                
                // draw item name (abbreviated)
                String itemName = item.getName();
                if (itemName.length() > 6) {
                    itemName = itemName.substring(0, 6);
                }
                gc.fillText(itemName, slotX + 5, slotY + 25);
                
                // draw slot number
                gc.fillText(String.valueOf(i + 1), slotX + 5, slotY + 12);
                
                // Draw quantity/ammo info below name
                String quantityText = "";
                if (item instanceof com.rust.exfil.takebradley.model.loot.ammo.AmmoItem) {
                    com.rust.exfil.takebradley.model.loot.ammo.AmmoItem ammo = 
                        (com.rust.exfil.takebradley.model.loot.ammo.AmmoItem) item;
                    quantityText = "x" + ammo.getQuantity();
                } else if (item instanceof WeaponItem) {
                    WeaponItem weapon = (WeaponItem) item;
                    quantityText = weapon.getCurrentAmmo() + "/" + weapon.getMagazineSize();
                } else if (item instanceof com.rust.exfil.takebradley.model.loot.medical.MedicalItem) {
                    com.rust.exfil.takebradley.model.loot.medical.MedicalItem medItem = 
                        (com.rust.exfil.takebradley.model.loot.medical.MedicalItem) item;
                    quantityText = "x" + medItem.getQuantity();
                }
                
                if (!quantityText.isEmpty()) {
                    gc.fillText(quantityText, slotX + 5, slotY + 40);
                }
            } else {
                // draw empty slot indicator
                gc.setFill(Color.GRAY);
                gc.setFont(HOTBAR_FONT);
                gc.fillText(String.valueOf(i + 1), slotX + 5, slotY + 12);
            }
        }
    }
}

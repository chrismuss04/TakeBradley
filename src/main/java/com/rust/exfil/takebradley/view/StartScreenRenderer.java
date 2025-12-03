package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.medical.MedicalItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class StartScreenRenderer {
    
    // Layout constants
    private static final double TITLE_Y = 50;
    private static final double STASH_X = 100;
    private static final double STASH_Y = 100;
    private static final double STASH_WIDTH = 600;
    private static final double STASH_HEIGHT = 300;
    private static final double SLOT_SIZE = 50;
    private static final double SLOT_SPACING = 5;
    private static final int STASH_COLUMNS = 10;
    
    private static final double LOADOUT_X = 100;
    private static final double LOADOUT_Y = 420;
    private static final double LOADOUT_WIDTH = 600;
    private static final double LOADOUT_HEIGHT = 100;
    
    private static final double BUTTON_WIDTH = 200;
    private static final double BUTTON_HEIGHT = 60;
    
    // Fonts
    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 32);
    private static final Font SECTION_FONT = Font.font("Arial", FontWeight.BOLD, 20);
    private static final Font ITEM_FONT = Font.font("Arial", 10);
    private static final Font BUTTON_FONT = Font.font("Arial", FontWeight.BOLD, 18);
    
    // Scroll state for stash view
    private int scrollOffset = 0;
    private static final int MAX_VISIBLE_ROWS = 5;
    
    // Selection state
    private enum FocusArea { STASH, LOADOUT, BUTTON }
    private FocusArea currentFocus = FocusArea.STASH;
    private int selectedStashIndex = 0;
    private int selectedLoadoutIndex = 0;
    
    public void renderStartScreen(GraphicsContext gc, Stash stash, Inventory loadout) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        
        // Clear background
        gc.setFill(Color.rgb(30, 30, 35));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // Render title
        renderTitle(gc, canvasWidth);
        
        // Render stash view (left side)
        renderStashView(gc, stash);
        
        // Render loadout view (right side)
        renderLoadoutView(gc, loadout);
        
        // Render start raid button (center bottom)
        renderStartButton(gc, canvasWidth, canvasHeight);
    }
    
    private void renderTitle(GraphicsContext gc, double canvasWidth) {
        gc.setFill(Color.WHITE);
        gc.setFont(TITLE_FONT);
        
        String title = "Take Bradley - Extraction Shooter";
        double titleWidth = computeTextWidth(title, TITLE_FONT);
        double titleX = (canvasWidth - titleWidth) / 2;
        
        gc.fillText(title, titleX, TITLE_Y);
    }
    
    private void renderStashView(GraphicsContext gc, Stash stash) {
        // Draw panel background
        gc.setFill(Color.rgb(40, 40, 45));
        gc.fillRect(STASH_X, STASH_Y, STASH_WIDTH, STASH_HEIGHT);
        
        // Draw panel border
        gc.setStroke(Color.rgb(100, 100, 100));
        gc.setLineWidth(2);
        gc.strokeRect(STASH_X, STASH_Y, STASH_WIDTH, STASH_HEIGHT);
        
        // Draw section title
        gc.setFill(Color.WHITE);
        gc.setFont(SECTION_FONT);
        gc.fillText("Stash", STASH_X + 10, STASH_Y - 10);
        
        // Draw stash grid (10x10 = 100 slots)
        double gridStartX = STASH_X + 10;
        double gridStartY = STASH_Y + 10;
        
        int totalRows = (stash.getSize() + STASH_COLUMNS - 1) / STASH_COLUMNS;
        int visibleRows = Math.min(MAX_VISIBLE_ROWS, totalRows);
        
        for (int row = scrollOffset; row < scrollOffset + visibleRows && row < totalRows; row++) {
            for (int col = 0; col < STASH_COLUMNS; col++) {
                int slotIndex = row * STASH_COLUMNS + col;
                
                if (slotIndex >= stash.getSize()) {
                    break;
                }
                
                double slotX = gridStartX + col * (SLOT_SIZE + SLOT_SPACING);
                double slotY = gridStartY + (row - scrollOffset) * (SLOT_SIZE + SLOT_SPACING);
                
                // Check if slot is within visible area
                if (slotY + SLOT_SIZE > STASH_Y + STASH_HEIGHT - 10) {
                    break;
                }
                
                renderStashSlot(gc, stash, slotIndex, slotX, slotY);
            }
        }
        
        // Draw scroll indicator if needed
        if (totalRows > MAX_VISIBLE_ROWS) {
            renderScrollIndicator(gc, totalRows);
        }
    }
    
    // helper render method to render a single slot
    private void renderStashSlot(GraphicsContext gc, Stash stash, int slotIndex, double x, double y) {
        LootItem item = stash.getItem(slotIndex);
        boolean isSelected = (currentFocus == FocusArea.STASH && slotIndex == selectedStashIndex);
        
        // Draw slot background
        if (isSelected && item != null) {
            gc.setFill(Color.rgb(100, 100, 150));
        } else {
            gc.setFill(Color.rgb(50, 50, 50));
        }
        gc.fillRect(x, y, SLOT_SIZE, SLOT_SIZE);
        
        // Draw slot border
        if (isSelected && item != null) {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(3);
        } else {
            gc.setStroke(Color.rgb(80, 80, 80));
            gc.setLineWidth(1);
        }
        gc.strokeRect(x, y, SLOT_SIZE, SLOT_SIZE);
        
        // Draw item if present
        if (item != null) {
            gc.setFill(Color.WHITE);
            gc.setFont(ITEM_FONT);
            
            // Draw item name (abbreviated)
            String itemName = item.getName();
            if (itemName.length() > 8) {
                itemName = itemName.substring(0, 8);
            }
            gc.fillText(itemName, x + 3, y + 20);
            
            // Draw quantity/ammo info
            String quantityText = getItemQuantityText(item);
            if (!quantityText.isEmpty()) {
                gc.fillText(quantityText, x + 3, y + 35);
            }
        } else {
            // Draw empty slot indicator
            gc.setFill(Color.GRAY);
            gc.setFont(ITEM_FONT);
            gc.fillText("-", x + 20, y + 30);
        }
    }
    
    private void renderScrollIndicator(GraphicsContext gc, int totalRows) {
        double indicatorX = STASH_X + STASH_WIDTH - 30;
        double indicatorY = STASH_Y + 10;
        double indicatorHeight = STASH_HEIGHT - 20;
        
        // Draw scrollbar background
        gc.setFill(Color.rgb(60, 60, 60));
        gc.fillRect(indicatorX, indicatorY, 20, indicatorHeight);
        
        // Draw scroll thumb
        double thumbHeight = indicatorHeight * MAX_VISIBLE_ROWS / totalRows;
        double thumbY = indicatorY + (indicatorHeight - thumbHeight) * scrollOffset / (totalRows - MAX_VISIBLE_ROWS);
        
        gc.setFill(Color.rgb(100, 100, 100));
        gc.fillRect(indicatorX, thumbY, 20, thumbHeight);
    }
    
    private void renderLoadoutView(GraphicsContext gc, Inventory loadout) {
        // Draw panel background
        gc.setFill(Color.rgb(40, 40, 45));
        gc.fillRect(LOADOUT_X, LOADOUT_Y, LOADOUT_WIDTH, LOADOUT_HEIGHT);
        
        // Draw panel border
        gc.setStroke(Color.rgb(100, 100, 100));
        gc.setLineWidth(2);
        gc.strokeRect(LOADOUT_X, LOADOUT_Y, LOADOUT_WIDTH, LOADOUT_HEIGHT);
        
        // Draw section title
        gc.setFill(Color.WHITE);
        gc.setFont(SECTION_FONT);
        gc.fillText("Raid Loadout", LOADOUT_X + 10, LOADOUT_Y - 10);
        
        // Draw loadout slots (10 slots in a single row)
        double gridStartX = LOADOUT_X + 10;
        double gridStartY = LOADOUT_Y + 50;
        
        for (int i = 0; i < loadout.getSize(); i++) {
            double slotX = gridStartX + i * (SLOT_SIZE + SLOT_SPACING);
            double slotY = gridStartY;
            
            renderLoadoutSlot(gc, loadout, i, slotX, slotY);
        }
        
        // Draw instructions below loadout
        gc.setFill(Color.rgb(200, 200, 200));
        gc.setFont(ITEM_FONT);
        double instructY = LOADOUT_Y + LOADOUT_HEIGHT + 20;
        gc.fillText("WASD/Arrows: Navigate | TAB: Switch Focus (Stash/Loadout/Button)", LOADOUT_X + 10, instructY);
        gc.fillText("E: Transfer Item | ENTER: Start Raid (when button selected)", LOADOUT_X + 10, instructY + 15);
    }
    
    // helper to render loadout slots
    private void renderLoadoutSlot(GraphicsContext gc, Inventory loadout, int slotIndex, double x, double y) {
        LootItem item = loadout.getItem(slotIndex);
        boolean isSelected = (currentFocus == FocusArea.LOADOUT && slotIndex == selectedLoadoutIndex);
        
        // Draw slot background
        if (isSelected) {
            gc.setFill(Color.rgb(100, 100, 150));
        } else {
            gc.setFill(Color.rgb(50, 50, 50));
        }
        gc.fillRect(x, y, SLOT_SIZE, SLOT_SIZE);
        
        // Draw slot border
        if (isSelected) {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(3);
        } else {
            gc.setStroke(Color.rgb(100, 100, 150));
            gc.setLineWidth(2);
        }
        gc.strokeRect(x, y, SLOT_SIZE, SLOT_SIZE);
        
        // Draw slot number
        gc.setFill(Color.GRAY);
        gc.setFont(ITEM_FONT);
        gc.fillText(String.valueOf(slotIndex + 1), x + 3, y + 12);
        
        // Draw item if present
        if (item != null) {
            gc.setFill(Color.WHITE);
            gc.setFont(ITEM_FONT);
            
            // Draw item name (abbreviated)
            String itemName = item.getName();
            if (itemName.length() > 8) {
                itemName = itemName.substring(0, 8);
            }
            gc.fillText(itemName, x + 3, y + 25);
            
            // Draw quantity/ammo info
            String quantityText = getItemQuantityText(item);
            if (!quantityText.isEmpty()) {
                gc.fillText(quantityText, x + 3, y + 40);
            }
        }
    }
    
    private void renderStartButton(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        double buttonX = (canvasWidth - BUTTON_WIDTH) / 2;
        double buttonY = canvasHeight - 80;
        
        boolean isSelected = (currentFocus == FocusArea.BUTTON);
        
        // Draw button background
        if (isSelected) {
            gc.setFill(Color.rgb(80, 160, 80)); // Brighter when selected
        } else {
            gc.setFill(Color.rgb(60, 120, 60));
        }
        gc.fillRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        
        // Draw button border
        if (isSelected) {
            gc.setStroke(Color.YELLOW); // Yellow when selected
            gc.setLineWidth(4);
        } else {
            gc.setStroke(Color.rgb(100, 180, 100));
            gc.setLineWidth(3);
        }
        gc.strokeRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        
        // Draw button text
        gc.setFill(Color.WHITE);
        gc.setFont(BUTTON_FONT);
        String buttonText = "Start Raid";
        double textWidth = computeTextWidth(buttonText, BUTTON_FONT);
        double textX = buttonX + (BUTTON_WIDTH - textWidth) / 2;
        double textY = buttonY + (BUTTON_HEIGHT + 15) / 2;
        gc.fillText(buttonText, textX, textY);
    }
    
    /**
     * Get quantity text for an item
     */
    private String getItemQuantityText(LootItem item) {
        if (item instanceof AmmoItem) {
            AmmoItem ammo = (AmmoItem) item;
            return "x" + ammo.getQuantity();
        } else if (item instanceof WeaponItem) {
            WeaponItem weapon = (WeaponItem) item;
            return weapon.getCurrentAmmo() + "/" + weapon.getMagazineSize();
        } else if (item instanceof MedicalItem) {
            MedicalItem med = (MedicalItem) item;
            return "x" + med.getQuantity();
        }
        return "";
    }
    
    private double computeTextWidth(String text, Font font) {
        // Approximate text width (JavaFX doesn't provide easy way to measure)
        return text.length() * font.getSize() * 0.6;
    }
    
    public void scrollUp() {
        if (scrollOffset > 0) {
            scrollOffset--;
        }
    }
    
    public void scrollDown(int totalRows) {
        if (scrollOffset < totalRows - MAX_VISIBLE_ROWS) {
            scrollOffset++;
        }
    }
    
    public int getScrollOffset() {
        return scrollOffset;
    }

    public void resetScroll() {
        scrollOffset = 0;
    }
    
    // ===== Navigation Methods =====
    // move selected slot up/down/left/right
    
    public void selectUp(Stash stash) {
        if (currentFocus == FocusArea.STASH) {
            // Move up one row in stash
            int targetIndex = selectedStashIndex - STASH_COLUMNS;
            if (targetIndex < 0) {
                // Wrap to bottom
                int rows = (stash.getSize() + STASH_COLUMNS - 1) / STASH_COLUMNS;
                targetIndex = selectedStashIndex + (rows - 1) * STASH_COLUMNS;
                if (targetIndex >= stash.getSize()) {
                    targetIndex -= STASH_COLUMNS;
                }
            }
            selectedStashIndex = findNearestNonEmptySlot(stash, targetIndex);
            ensureSelectedVisible(stash);
        }
    }
    
    public void selectDown(Stash stash) {
        if (currentFocus == FocusArea.STASH) {
            // Move down one row in stash
            int targetIndex = selectedStashIndex + STASH_COLUMNS;
            if (targetIndex >= stash.getSize()) {
                // Wrap to top
                targetIndex = selectedStashIndex % STASH_COLUMNS;
            }
            selectedStashIndex = findNearestNonEmptySlot(stash, targetIndex);
            ensureSelectedVisible(stash);
        }
    }
    
    public void selectLeft(Stash stash, Inventory loadout) {
        if (currentFocus == FocusArea.STASH) {
            int targetIndex = selectedStashIndex - 1;
            if (targetIndex < 0) {
                targetIndex = stash.getSize() - 1;
            }
            selectedStashIndex = findNearestNonEmptySlot(stash, targetIndex);
            ensureSelectedVisible(stash);
        } else {
            // Loadout
            selectedLoadoutIndex--;
            if (selectedLoadoutIndex < 0) {
                selectedLoadoutIndex = loadout.getSize() - 1;
            }
        }
    }
    
    public void selectRight(Stash stash, Inventory loadout) {
        if (currentFocus == FocusArea.STASH) {
            int targetIndex = selectedStashIndex + 1;
            if (targetIndex >= stash.getSize()) {
                targetIndex = 0;
            }
            selectedStashIndex = findNearestNonEmptySlot(stash, targetIndex);
            ensureSelectedVisible(stash);
        } else {
            // Loadout
            selectedLoadoutIndex++;
            if (selectedLoadoutIndex >= loadout.getSize()) {
                selectedLoadoutIndex = 0;
            }
        }
    }
    
    // switch between stash, loadout, and start button
    public void switchFocus() {
        if (currentFocus == FocusArea.STASH) {
            currentFocus = FocusArea.LOADOUT;
        } else if (currentFocus == FocusArea.LOADOUT) {
            currentFocus = FocusArea.BUTTON;
        } else {
            currentFocus = FocusArea.STASH;
        }
    }
    
   // transfer from stash to loadout or loadout to stash
    public void transferSelectedItem(Stash stash, Inventory loadout) {
        if (currentFocus == FocusArea.STASH) {
            // transfer from stash to loadout
            LootItem item = stash.getItem(selectedStashIndex);
            if (item != null) {
                if (loadout.addItem(item)) {
                    stash.removeItem(selectedStashIndex);
                    
                    // Move selection to next non-empty slot
                    selectedStashIndex = findNearestNonEmptySlot(stash, selectedStashIndex);
                }
            }
        } else {
            // transfer from loadout to stash
            LootItem item = loadout.getItem(selectedLoadoutIndex);
            if (item != null) {
                if (stash.addItem(item)) {
                    loadout.removeItem(selectedLoadoutIndex);
                }
            }
        }
    }
    
    private int findNearestNonEmptySlot(Inventory inventory, int targetIndex) {
        int startIndex = targetIndex;
        
        // Try forward search first
        int index = targetIndex;
        do {
            if (inventory.getItem(index) != null) {
                return index;
            }
            index++;
            if (index >= inventory.getSize()) {
                index = 0;
            }
        } while (index != startIndex);
        
        // ff no non-empty slot found return target index
        return targetIndex;
    }
    
    // adjust scroll if needed
    private void ensureSelectedVisible(Stash stash) {
        int selectedRow = selectedStashIndex / STASH_COLUMNS;
        
        // If selected row is above visible area, scroll up
        if (selectedRow < scrollOffset) {
            scrollOffset = selectedRow;
        }
        
        // If selected row is below visible area, scroll down
        if (selectedRow >= scrollOffset + MAX_VISIBLE_ROWS) {
            scrollOffset = selectedRow - MAX_VISIBLE_ROWS + 1;
        }
        
        // Clamp scroll offset
        int totalRows = (stash.getSize() + STASH_COLUMNS - 1) / STASH_COLUMNS;
        int maxScroll = Math.max(0, totalRows - MAX_VISIBLE_ROWS);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
    }
    
    public int getSelectedStashIndex() {
        return selectedStashIndex;
    }
    
    public int getSelectedLoadoutIndex() {
        return selectedLoadoutIndex;
    }
    
    public FocusArea getCurrentFocus() {
        return currentFocus;
    }
    
    public boolean isStashFocused() {
        return currentFocus == FocusArea.STASH;
    }
    
    public boolean isLoadoutFocused() {
        return currentFocus == FocusArea.LOADOUT;
    }
    
    public boolean isButtonFocused() {
        return currentFocus == FocusArea.BUTTON;
    }
    
    public void reset(Stash stash) {
        currentFocus = FocusArea.STASH;
        selectedStashIndex = findNearestNonEmptySlot(stash, 0);
        selectedLoadoutIndex = 0;
        scrollOffset = 0;
    }
}

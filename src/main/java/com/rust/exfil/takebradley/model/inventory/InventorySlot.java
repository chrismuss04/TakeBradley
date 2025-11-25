package com.rust.exfil.takebradley.model.inventory;

import com.rust.exfil.takebradley.model.loot.LootItem;

public class InventorySlot {
    private LootItem item;
    private boolean isEmpty;

    public InventorySlot() {
        this.item = null;
        this.isEmpty = true;
    }

    public boolean addItem(LootItem item) {
        if (!isEmpty) return false;
        this.item = item;
        this.isEmpty = false;
        return true;
    }

    public LootItem removeItem() {
        if (isEmpty) return null;
        LootItem removed = this.item;
        this.item = null;
        this.isEmpty = true;
        return removed;
    }

    public LootItem getItem() {
        return item;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}

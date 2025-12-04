package com.rust.exfil.takebradley.model.inventory;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.gear.GearItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;

import java.util.ArrayList;
import  java.util.List;

public class Inventory {
    private final List<InventorySlot> slots;
    private final int capacity;
    private Entity owner;
    private GearItem equippedGear;

    public Inventory(int capacity, Entity owner) {
        this.owner = owner;
        this.capacity = capacity;
        this.slots = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            slots.add(new InventorySlot());
        }
    }

    public void setEquippedGear(GearItem gear) {
        GearItem oldGear = null;
        if(equippedGear != null) {
            oldGear = removeEquippedGear();
        }
        this.equippedGear = gear;
        if (gear != null) {
            gear.applyBuffs(owner);
        }
        if (oldGear != null) {
            addItem(oldGear);
        }
    }

    public GearItem removeEquippedGear() {
        GearItem gear = equippedGear;
        if (gear != null) {
            gear.removeBuffs(owner);
            this.equippedGear = null;
        }
        return gear;
    }

    public GearItem getEquippedGear() {
        return equippedGear;
    }

    // add to first available slot, if all full return false
    public boolean addItem(LootItem item) {
        for (InventorySlot slot : slots) {
            if (slot.isEmpty()) {
                return slot.addItem(item);
            }
        }
        return false;
    }

    public LootItem getItem(int index) {
        if (isValidIndex(index)) {
            return slots.get(index).getItem();
        }
        return null;
    }

    public LootItem removeItem(int index) {
        if (isValidIndex(index)) {
            return slots.get(index).removeItem();
        }
        return null;
    }

    public List<LootItem> removeAllItems() {
        List<LootItem> removed = new ArrayList<>();
        for (InventorySlot slot : slots) {
            if (!slot.isEmpty()) {
                removed.add(slot.removeItem());
            }
        }
        return removed;
    }

    public int getSize() {
        return capacity;
    }

    boolean isValidIndex(int index) {
        return index >= 0 && index < capacity;
    }

    public int findAmmo(AmmoType ammoType) {
        for (int i = 0; i < slots.size(); i++) {
            LootItem item = slots.get(i).getItem();
            if (item instanceof AmmoItem) {
                AmmoItem ammo = (AmmoItem) item;
                if (ammo.getAmmoType() == ammoType) {
                    return i; // return slot index
                }
            }
        }
        return -1; // Not found
    }

    // equip first available gear - for npc/scientist spawn
    public void equipGear() {
        for (int i = 0; i < slots.size(); i++) {
            LootItem item = slots.get(i).getItem();
            if (item instanceof GearItem) {
                // remove from inventory before equipping to avoid duplication
                removeItem(i);
                setEquippedGear((GearItem) item);     
                return;
            }
        }
    }
    
    public boolean equipGearFromSlot(int slotIndex) {
        if (!isValidIndex(slotIndex)) {
            return false;
        }
        
        LootItem item = slots.get(slotIndex).getItem();
        if (item instanceof GearItem) {
            // remove from inventory before equipping to avoid duplication
            removeItem(slotIndex);
            setEquippedGear((GearItem) item);
            return true;
        }
        
        return false;
    }
    
    public boolean isEmpty() {
        for (InventorySlot slot : slots) {
            if (!slot.isEmpty()) {
                return false;
            }
        }
        return true;
    }

}

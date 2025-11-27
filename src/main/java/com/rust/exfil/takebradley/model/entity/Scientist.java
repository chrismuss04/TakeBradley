package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

import java.util.UUID;
import java.util.List;

public class Scientist implements Entity, Combatant {
    private final UUID id;
    private final String name;
    private double x, y;
    private int health;
    private final int maxHealth = 100;
    private boolean isAlive = true;
    private Inventory inventory;
    private int selectedSlotIndex = 0;

    Scientist(String name, double x, double y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = maxHealth;
        this.inventory = new Inventory(5);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void takeDamage(int damage) {
        if (!isAlive) return;
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            die();
        }
    }

    @Override
    public void heal(int amount) {
        if (!isAlive) return;
        this.health += amount;
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }

    @Override
    public void die() {
        this.isAlive = false;
    }

    @Override
    public void fireWeapon() {
        LootItem item = inventory.getItem(selectedSlotIndex);
        if (item instanceof WeaponItem) {
            item.use(this);
        }
    }

    @Override
    public void reload() {
        LootItem item = inventory.getItem(selectedSlotIndex);
        if (item instanceof WeaponItem) {
            ((WeaponItem) item).reload();
        }
    }

    @Override
    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void update(double deltaTime) {

    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void equipItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < inventory.getSize()) {
            this.selectedSlotIndex = slotIndex;

        }

    }

    public LootItem getEquippedItem() {
        return inventory.getItem(selectedSlotIndex);
    }
}

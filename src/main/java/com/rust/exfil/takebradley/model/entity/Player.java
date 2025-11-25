package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

import java.util.UUID;
import java.util.List;

public class Player implements Entity, Movable, Combatant {
    private final UUID id;
    private final String name;
    private double x, y;
    private int health;
    private final int maxHealth = 100;
    private double speed = 5.0;
    private boolean isAlive = true;
    private Inventory inventory;
    private Stash stash;
    private int selectedSlotIndex = 0;

    public Player(String name, double x, double y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = maxHealth;
        this.inventory = new Inventory(10);
        this.stash = new Stash();
    }


    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public void takeDamage(int damage) {
        if (!this.isAlive) return;
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            die();
        }

    }

    @Override
    public void heal(int amount) {
        if (!isAlive) return;
        this.health = Math.min(this.health + amount, maxHealth);
    }

    @Override
    public void die() {
        this.isAlive = false;
        //drop inventory items

    }

    @Override
    public void fireWeapon() {
        if (!isAlive) return;
        LootItem item = getEquippedItem();
        if (item != null) {
            item.use(this);
        }

    }

    @Override
    public void reload() {
        if (!isAlive) return;
        LootItem item = getEquippedItem();
        if (item instanceof WeaponItem) {
            ((WeaponItem) item).reload();
        }
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
        // Player-specific update logic can be added here
    }

    @Override
    public void move(double dx, double dy) {
        if (!isAlive) return;
        this.x += dx * speed;
        this.y += dy * speed;
    }

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Stash getStash() {
        return stash;
    }

    public void equipItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < inventory.getSize()) {
            this.selectedSlotIndex = slotIndex;

        }

    }

    public LootItem getEquippedItem() {
        return inventory.getItem(selectedSlotIndex);
    }

    public void extract() {
        List<LootItem> items = inventory.removeAllItems();
        stash.depositAll(items);
    }

}

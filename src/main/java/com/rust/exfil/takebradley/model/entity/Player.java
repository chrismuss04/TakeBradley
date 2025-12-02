package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.gear.GearItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

import java.util.UUID;
import java.util.List;

public class Player implements Entity, Movable, Combatant {
    private final UUID id;
    private final String name;
    private double x, y;
    private int health;
    private final int maxHealth = 100;
    private double speed = 2.0;
    private boolean isAlive = true;
    private double moveX = 0;  // Movement intent X
    private double moveY = 0;  // Movement intent Y
    private Inventory inventory;
    private Stash stash;
    private int selectedSlotIndex = 0;
    private double damageResistance = 0.0;
    private Direction facingDirection = com.rust.exfil.takebradley.model.Direction.RIGHT;

    Player(String name, double x, double y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = maxHealth;
        this.inventory = new Inventory(10, this);
        this.stash = new Stash(this);
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
        this.health -= (int) (damage * (1 - this.damageResistance));
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
        
        // Unequip gear so it can be looted
        if (inventory.getEquippedGear() != null) {
            GearItem gear = inventory.removeEquippedGear();
            // Try to add gear back to inventory if there's space
            inventory.addItem(gear);
        }
        
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
            WeaponItem weapon = (WeaponItem) item;
            
            // Find matching ammo in inventory
            int ammoSlot = inventory.findAmmo(weapon.getAmmoType());
            if (ammoSlot == -1) {
                return; // No ammo available
            }
            
            // Get the ammo item
            AmmoItem ammo = 
                (AmmoItem) inventory.getItem(ammoSlot);
            
            // Reload weapon and get leftover ammo
            int leftover = weapon.reload(ammo.getQuantity());
            
            // Update or remove ammo from inventory
            if (leftover > 0) {
                ammo.setQuantity(leftover);
            } else {
                inventory.removeItem(ammoSlot); // All ammo consumed
            }
        }
    }

    @Override
    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }

    @Override
    public double getDamageResistance() {
        return damageResistance;
    }

    @Override
    public void setDamageResistance(double resistance) {
        this.damageResistance = resistance;
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
        // Apply movement intent
        if (moveX != 0 || moveY != 0) {
            move(moveX, moveY);
            // Reset movement intent after applying
            moveX = 0;
            moveY = 0;
        }
        
        // Update equipped weapon (for reload timer)
        LootItem equipped = getEquippedItem();
        if (equipped instanceof WeaponItem) {
            ((WeaponItem) equipped).update();
        }
    }
    
    // Set movement intent (called by InputHandler)
    public void setMovementIntent(double dx, double dy) {
        this.moveX = dx;
        this.moveY = dy;
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

    @Override
    public Direction getFacingDirection() {
        return facingDirection;
    }

    @Override
    public void setFacingDirection(Direction direction) {
        this.facingDirection = direction;
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

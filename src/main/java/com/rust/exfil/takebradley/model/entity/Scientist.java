package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import com.rust.exfil.takebradley.model.Direction;

import java.util.UUID;

public class Scientist implements Entity, Combatant {
    private final UUID id;
    private final String name;
    private final double x, y;
    private int health;
    private final int maxHealth = 100;
    private boolean isAlive = true;
    private Inventory inventory;
    private int selectedSlotIndex = 0;
    private double damageResistance = 0.0;
    private Direction facingDirection = Direction.RIGHT;

    Scientist(String name, double x, double y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = maxHealth;
        this.inventory = new Inventory(5, this);
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
        this.health -= (int) (damage * (1 - damageResistance));
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
        if (!isAlive) return;
        LootItem item = inventory.getItem(selectedSlotIndex);
        if (item instanceof WeaponItem) {
            WeaponItem weapon = (WeaponItem) item;
            
            int ammoSlot = inventory.findAmmo(weapon.getAmmoType());
            if (ammoSlot == -1) {
                return;
            }
            
            AmmoItem ammo = 
                (AmmoItem) inventory.getItem(ammoSlot);
            
            int leftover = weapon.reload(ammo.getQuantity());
            
            if (leftover > 0) {
                ammo.setQuantity(leftover);
            } else {
                inventory.removeItem(ammoSlot);
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

    @Override
    public Direction getFacingDirection() {
        return facingDirection;
    }

    @Override
    public void setFacingDirection(Direction direction) {
        this.facingDirection = direction;
    }
}

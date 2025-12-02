package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import com.rust.exfil.takebradley.model.strategy.TankMovementStrategy;
import com.rust.exfil.takebradley.model.strategy.CombatStrategy;

import java.util.UUID;

public class BradleyAPC implements Entity, Movable, Combatant {
    private final UUID id;
    private final String name;
    private double x, y;
    private int health;
    private final int maxHealth = 1200;
    private double speed = 2.0;
    private boolean isAlive = true;
    private Inventory inventory;
    private int selectedSlotIndex = 0;
    private double damageResistance = 0.0;
    private Direction facingDirection = com.rust.exfil.takebradley.model.Direction.RIGHT;
    private final CombatStrategy combatStrategy;
    private GameWorld gameWorld;

    BradleyAPC(String name, double x, double y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = maxHealth;
        this.inventory = new Inventory(1, this);
        this.combatStrategy = new TankMovementStrategy();
    }
    
    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
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
        if  (!isAlive) return;
        LootItem item = inventory.getItem(0);
        if (item instanceof WeaponItem) {
            item.use(this);
        }
    }

    @Override
    public void reload() {
        if (!isAlive) return;
        LootItem item = inventory.getItem(0);
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
        if (!isAlive || gameWorld == null) return;
        
        // Execute movement strategy (handles both movement and combat)
        combatStrategy.execute(this, gameWorld, deltaTime);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void equipItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < inventory.getSize()) {
            this.selectedSlotIndex = slotIndex;

        }

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
        this.speed=speed;
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

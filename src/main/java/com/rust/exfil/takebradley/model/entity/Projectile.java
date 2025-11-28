package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.inventory.Inventory;

import java.util.UUID;

public class Projectile implements Entity {
    private final UUID id;
    private final String name;
    private double x, y;
    private final double velocityX, velocityY;
    private final int damage;
    private final Entity owner;
    private final double speed;
    private boolean isAlive;

    public Projectile(double x, double y, double velocityX, double velocityY, int damage, Entity owner, double speed) {
        this.id = UUID.randomUUID();
        this.name = "Projectile";
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.damage = damage;
        this.owner = owner;
        this.speed = speed;
        this.isAlive = true;
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
        if (!isAlive) return;

        // Move projectile based on velocity and speed
        x += velocityX * speed * deltaTime;
        y += velocityY * speed * deltaTime;

        // Note: Wall collision and entity collision will be checked by GameWorld
        // GameWorld will call checkCollisions() and handle removal
    }

    @Override
    public Inventory getInventory() {
        return null; // Projectiles don't have inventory
    }

    public Entity getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    public void hit(Entity target) {
        if (!isAlive) return;
        if (target == owner) return; // No friendly fire

        if (target instanceof Combatant) {
            ((Combatant) target).takeDamage(damage);
        }

        isAlive = false; // Projectile is destroyed on hit
    }

    public void hitWall() {
        isAlive = false; // Projectile is destroyed when hitting wall
    }

    public boolean isCollidingWith(Entity entity) {
        if (entity == owner) return false; // Can't collide with owner

        double distance = calculateDistance(entity.getX(), entity.getY());
        return distance < 10.0; // Collision radius of 10 units
    }

    private double calculateDistance(double targetX, double targetY) {
        double dx = targetX - x;
        double dy = targetY - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.controller.EventPublisher;
import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.entity.Projectile;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;
import com.rust.exfil.takebradley.systems.event.ProjectileCreatedEvent;

public abstract class WeaponItem implements LootItem {
    String name;
    String description;
    int magazineSize;
    int damage;
    int currentAmmo;
    double projectileSpeed;

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getMagazineSize() {
        return magazineSize;
    }
    public int getDamage() {
        return damage;
    }
    public int getCurrentAmmo() {
        return currentAmmo;
    }
    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = currentAmmo;
    }
    public double getProjectileSpeed() {
        return projectileSpeed;
    }
    
    public abstract AmmoType getAmmoType();
    public abstract int reload(int availableAmmo);

    @Override
    public void use(Entity user) {
        // Check if we have ammo
        if (currentAmmo <= 0) {
            return;
        }

        // Get direction from user if they're a combatant, otherwise default to RIGHT
        Direction direction = Direction.RIGHT;
        if (user instanceof Combatant) {
            direction = ((Combatant) user).getFacingDirection();
        }

        // Create projectile at user's position
        Projectile projectile = new Projectile(
            user.getX(),
            user.getY(),
            direction.getDx(),
            direction.getDy(),
            damage,
            user,
            projectileSpeed
        );

        // Publish event so GameController can add it to the world
        EventPublisher.getInstance().publish(new ProjectileCreatedEvent(projectile));

        // Consume ammo
        currentAmmo--;
    }
}

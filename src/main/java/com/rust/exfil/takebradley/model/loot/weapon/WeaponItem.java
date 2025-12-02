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
    long reloadDuration; // Reload time in milliseconds
    
    // Reload state tracking
    private boolean isReloading = false;
    private long reloadStartTime = 0;
    private int ammoToLoad = 0; // Ammo that will be loaded when reload completes

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
    public long getReloadDuration() {
        return reloadDuration;
    }
    public boolean isReloading() {
        return isReloading;
    }
    
    // Public method to update weapon state (should be called every frame)
    public void update() {
        checkReloadComplete();
    }
    
    // Check if reload is complete and finalize it
    private void checkReloadComplete() {
        if (isReloading && System.currentTimeMillis() - reloadStartTime >= reloadDuration) {
            // Complete the reload
            this.currentAmmo += ammoToLoad;
            ammoToLoad = 0;
            isReloading = false;
        }
    }
    
    public abstract AmmoType getAmmoType();
    public abstract int reload(int availableAmmo);

    @Override
    public void use(Entity user) {
        // Check if reload is complete
        checkReloadComplete();
        
        // Can't shoot while reloading
        if (isReloading) {
            return;
        }
        
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
        this.currentAmmo--;
    }
    
    // Start reload process - returns leftover ammo immediately
    protected int startReload(int availableAmmo) {
        // Can't reload if already reloading
        if (isReloading) {
            return availableAmmo;
        }
        
        // Can't reload if magazine is full
        if (currentAmmo >= magazineSize) {
            return availableAmmo;
        }
        
        // Calculate how much ammo we'll load
        int needed = magazineSize - currentAmmo;
        ammoToLoad = Math.min(needed, availableAmmo);
        
        // Start reload timer
        isReloading = true;
        reloadStartTime = System.currentTimeMillis();
        
        // Return leftover ammo (ammo is consumed immediately from inventory)
        return availableAmmo - ammoToLoad;
    }
}

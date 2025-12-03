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
    WeaponType weaponType;
    int magazineSize;
    int damage;
    int currentAmmo;
    double projectileSpeed;
    long reloadDuration; // Reload time in milliseconds
    boolean isFullAuto; // True for full-auto, false for semi-auto
    int roundsPerMinute; // Fire rate for full-auto weapons
    double bloom; // Weapon spread/inaccuracy in radians (0 = perfectly accurate)
    
    // Reload state tracking
    private boolean isReloading = false;
    private long reloadStartTime = 0;
    private int ammoToLoad = 0; // Ammo that will be loaded when reload completes
    
    // Fire rate tracking for full-auto
    private long lastFireTime = 0;

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
    
    public boolean isFullAuto() {
        return isFullAuto;
    }
    
    public int getRoundsPerMinute() {
        return roundsPerMinute;
    }
    
    public WeaponType getWeaponType() {
        return weaponType;
    }
    
    // Public method to update weapon state (should be called every frame)
    public void update() {
        checkReloadComplete();
    }
    
    /**
     * Check if weapon can fire based on fire rate
     * For full-auto weapons, enforces RPM limit
     * For semi-auto weapons, always returns true (handled by input)
     */
    public boolean canFire() {
        if (!isFullAuto) {
            return true; // Semi-auto handled by input system
        }
        
        // Calculate minimum time between shots based on RPM
        long timeBetweenShots = 60000 / roundsPerMinute; // milliseconds
        long currentTime = System.currentTimeMillis();
        
        return (currentTime - lastFireTime) >= timeBetweenShots;
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
        
        // Check fire rate for full-auto weapons
        if (!canFire()) {
            return;
        }

        // Get direction from user if they're a combatant, otherwise default to RIGHT
        Direction direction = Direction.RIGHT;
        if (user instanceof Combatant) {
            direction = ((Combatant) user).getFacingDirection();
        }

        // Apply bloom (weapon spread)
        double baseDx = direction.getDx();
        double baseDy = direction.getDy();
        
        // Add random spread based on bloom value
        if (bloom > 0) {
            // Random angle offset within bloom cone
            double spreadAngle = (Math.random() - 0.5) * 2 * bloom;
            
            // Calculate current angle
            double currentAngle = Math.atan2(baseDy, baseDx);
            
            // Apply spread
            double newAngle = currentAngle + spreadAngle;
            baseDx = Math.cos(newAngle);
            baseDy = Math.sin(newAngle);
        }

        // Create projectile at user's position with bloom-adjusted direction
        Projectile projectile = new Projectile(
            user.getX(),
            user.getY(),
            baseDx,
            baseDy,
            damage,
            user,
            projectileSpeed
        );

        // Publish event so GameController can add it to the world
        EventPublisher.getInstance().publish(new ProjectileCreatedEvent(projectile));

        // Consume ammo
        this.currentAmmo--;
        
        // Update last fire time
        lastFireTime = System.currentTimeMillis();
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

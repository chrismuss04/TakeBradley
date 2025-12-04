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
    long reloadDuration; // reload time in milliseconds
    boolean isFullAuto; 
    int roundsPerMinute; // fire rate for full-auto weapons
    double bloom; // weapon spread/inaccuracy in radians
    
    // reload state tracking
    private boolean isReloading = false;
    private long reloadStartTime = 0;
    private int ammoToLoad = 0; 
    
    // fire rate tracking for full-auto
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
    
    public void update() {
        checkReloadComplete();
    }
    
    
    public boolean canFire() {
        if (!isFullAuto) {
            return true;
        }
        
        // calculate minimum time between shots based on RPM
        long timeBetweenShots = 60000 / roundsPerMinute; // milliseconds
        long currentTime = System.currentTimeMillis();
        
        return (currentTime - lastFireTime) >= timeBetweenShots;
    }
    
    private void checkReloadComplete() {
        if (isReloading && System.currentTimeMillis() - reloadStartTime >= reloadDuration) {
            // complete the reload
            this.currentAmmo += ammoToLoad;
            ammoToLoad = 0;
            isReloading = false;
        }
    }
    
    public abstract AmmoType getAmmoType();
    public abstract int reload(int availableAmmo);

    @Override
    public void use(Entity user) {
        checkReloadComplete();
        
        if (isReloading) {
            return;
        }
        
        if (currentAmmo <= 0) {
            return;
        }
        
        if (!canFire()) {
            return;
        }

        // get direction from user if they're a combatant for fire direction
        Direction direction = Direction.RIGHT;
        if (user instanceof Combatant) {
            direction = ((Combatant) user).getFacingDirection();
        }

        // apply bloom
        double baseDx = direction.getDx();
        double baseDy = direction.getDy();
        if (bloom > 0) {
            // random angle offset within bloom cone
            double spreadAngle = (Math.random() - 0.5) * 2 * bloom;
            
            // calculate current angle
            double currentAngle = Math.atan2(baseDy, baseDx);
            
            // apply spread
            double newAngle = currentAngle + spreadAngle;
            baseDx = Math.cos(newAngle);
            baseDy = Math.sin(newAngle);
        }

        // create projectile at user's position with bloom-adjusted direction
        Projectile projectile = new Projectile(
            user.getX(),
            user.getY(),
            baseDx,
            baseDy,
            damage,
            user,
            projectileSpeed
        );

        // publish event
        EventPublisher.getInstance().publish(new ProjectileCreatedEvent(projectile));

        // consume ammo
        this.currentAmmo--;
        
        // update last fire time
        lastFireTime = System.currentTimeMillis();
    }
    
    protected int startReload(int availableAmmo) {
        if (isReloading) {
            return availableAmmo;
        }
        
        if (currentAmmo >= magazineSize) {
            return availableAmmo;
        }
        
        // calculate how much ammo we'll load
        int needed = magazineSize - currentAmmo;
        ammoToLoad = Math.min(needed, availableAmmo);
        
        // start reload timer
        isReloading = true;
        reloadStartTime = System.currentTimeMillis();
        
        // return leftover ammo to add back to inventory
        return availableAmmo - ammoToLoad;
    }
}

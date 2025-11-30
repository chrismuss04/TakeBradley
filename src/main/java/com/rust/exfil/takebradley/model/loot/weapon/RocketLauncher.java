package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;

public class RocketLauncher extends WeaponItem{
    public RocketLauncher() {
        this.name = "Rocket Launcher";
        this.description = "A powerful explosive weapon";
        this.magazineSize = 1;
        this.damage = 100;
        this.currentAmmo = 1;
        this.projectileSpeed = 400.0;
    }

    @Override
    public AmmoType getAmmoType() {
        return AmmoType.ROCKET;
    }

    @Override
    public int reload(int availableAmmo) {
        int needed = magazineSize - currentAmmo;
        int toReload = Math.min(needed, availableAmmo);
        currentAmmo += toReload;
        return availableAmmo - toReload;
    }
}

package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;

public class AK extends WeaponItem{
    public AK() {
        this.name = "AK";
        this.description = "A powerful assault rifle";
        this.magazineSize = 30;
        this.damage = 35;
        this.currentAmmo = 30;
        this.projectileSpeed = 800.0;
    }

    @Override
    public AmmoType getAmmoType() {
        return AmmoType.RIFLE;
    }

    @Override
    public int reload(int availableAmmo) {
        int needed = magazineSize - currentAmmo;
        int toReload = Math.min(needed, availableAmmo);
        currentAmmo += toReload;
        return availableAmmo - toReload;
    }
}

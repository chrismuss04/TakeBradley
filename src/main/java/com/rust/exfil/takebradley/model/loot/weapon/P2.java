package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;

public class P2 extends WeaponItem {
    P2() {
        this.name = "P2";
        this.description = "A semi-automatic pistol";
        this.magazineSize = 10;
        this.damage = 15;
        this.currentAmmo = 10;
        this.projectileSpeed = 600.0;
    }

    @Override
    public AmmoType getAmmoType() {
        return AmmoType.PISTOL;
    }

    @Override
    public int reload(int availableAmmo) {
        int needed = magazineSize - currentAmmo;
        int toReload = Math.min(needed, availableAmmo);
        currentAmmo += toReload;
        return availableAmmo - toReload;
    }
}

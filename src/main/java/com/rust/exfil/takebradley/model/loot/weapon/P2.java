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
        this.reloadDuration = 1500; // 1.5 seconds to reload
    }

    @Override
    public AmmoType getAmmoType() {
        return AmmoType.PISTOL;
    }

    @Override
    public int reload(int availableAmmo) {
        return startReload(availableAmmo);
    }
}

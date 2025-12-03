package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;

public class P2 extends WeaponItem {
    P2() {
        this.name = "P2";
        this.description = "A semi-automatic pistol";
        this.magazineSize = 10;
        this.damage = 18;
        this.currentAmmo = 10;
        this.projectileSpeed = 600.0;
        this.reloadDuration = 1500; // 1.5 seconds to reload
        this.isFullAuto = false; // Semi-auto
        this.roundsPerMinute = 0; // Not used for semi-auto
        this.bloom = 0.10; // Low spread (~6 degrees) - pistol is more accurate
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

package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;

public class MP5 extends WeaponItem{
    MP5() {
        this.name = "MP5";
        this.description = "A compact submachine gun";
        this.magazineSize = 30;
        this.damage = 20;
        this.currentAmmo = 30;
        this.projectileSpeed = 700.0;
        this.reloadDuration = 1800; // 1.8 seconds to reload
        this.isFullAuto = true;
        this.roundsPerMinute = 600; // 600 RPM (faster than AK)
        this.bloom = 0.18; // Higher spread (~10 degrees) - less accurate but faster fire
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

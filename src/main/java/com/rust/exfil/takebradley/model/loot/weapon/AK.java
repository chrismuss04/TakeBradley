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
        this.reloadDuration = 2000; // 2 seconds to reload
        this.isFullAuto = true;
        this.roundsPerMinute = 450; // 450 RPM 
        this.bloom = 0.12; // Moderate spread (~7 degrees)
    }

    @Override
    public AmmoType getAmmoType() {
        return AmmoType.RIFLE;
    }

    @Override
    public int reload(int availableAmmo) {
        return startReload(availableAmmo);
    }
}

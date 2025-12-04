package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;

public class RocketLauncher extends WeaponItem{
    public RocketLauncher() {
        this.name = "Rocket Launcher";
        this.description = "A powerful explosive weapon";
        this.weaponType = WeaponType.ROCKET_LAUNCHER;
        this.magazineSize = 1;
        this.damage = 100;
        this.currentAmmo = 1;
        this.projectileSpeed = 400.0;
        this.reloadDuration = 3000; // 3 seconds to reload
        this.isFullAuto = false; // semi-auto (one shot per trigger pull)
        this.roundsPerMinute = 0;
        this.bloom = 0.0;
    }

    @Override
    public AmmoType getAmmoType() {
        return AmmoType.ROCKET;
    }

    @Override
    public int reload(int availableAmmo) {
        return startReload(availableAmmo);
    }
}

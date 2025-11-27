package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class RocketLauncher extends WeaponItem{
    public RocketLauncher() {
        this.name = "Rocket Launcher";
        this.description = "A heavy weapon that fires explosive rockets.";
        this.magazineSize = 1;
        this.damage = 100;
    }

    @Override
    public void reload() {
        // Reload logic for Rocket Launcher - ensure rocket ammo is available
    }

    @Override
    public void use(Entity user) {
        // shooting logic for Rocket Launcher

    }
}

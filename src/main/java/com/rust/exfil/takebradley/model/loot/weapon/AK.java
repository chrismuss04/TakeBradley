package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class AK extends WeaponItem{
    AK() {
        this.name = "AK";
        this.description = "A powerful assault rifle";
        this.magazineSize = 30;
        this.damage = 35;
    }

    @Override
    public void reload() {
        // Reload logic for AK - ensure rifle ammo is available
    }

    @Override
    public void use(Entity user) {
        // shooting logic for AK
    }
}

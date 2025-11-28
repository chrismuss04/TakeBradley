package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class MP5 extends WeaponItem{
    MP5() {
        this.name = "MP5";
        this.description = "A compact submachine gun";
        this.magazineSize = 30;
        this.damage = 20;
    }

    @Override
    public void reload() {
        // Reload logic for MP5 - ensure pistol ammo is available
    }

    @Override
    public void use(Entity user) {
        // shooting logic for MP5

    }
}

package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

public class P2 extends WeaponItem {
    P2() {
        this.name = "P2";
        this.description = "A semi-automatic pistol chambered in 9mm.";
        this.magazineSize = 10;
        this.damage = 15;
    }
    @Override
    public void reload() {
        // Reload logic for P2 pistol - ensure pistol ammo is available
    }

    @Override
    public void use(Entity user) {
        // shooting logic for P2 pistol
    }
}

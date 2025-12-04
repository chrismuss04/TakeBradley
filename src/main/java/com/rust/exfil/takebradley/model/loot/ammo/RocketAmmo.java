package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class RocketAmmo extends AmmoItem{
    RocketAmmo() {
        this.name = "Rocket Ammo";
        this.description = "Ammunition for rocket launchers.";
        this.quantity = 3;
        this.ammoType = AmmoType.ROCKET;
    }

    @Override
    public void use(Entity user) {
        //do nothing
    }
}

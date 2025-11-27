package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class RocketAmmo extends AmmoItem{
    public RocketAmmo() {
        this.name = "Rocket Ammo";
        this.description = "Ammunition for rocket launchers.";
    }

    @Override
    public void use(Entity user) {
        //do nothing - can't 'use' ammo directly
    }
}

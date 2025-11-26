package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class RifleAmmo extends AmmoItem{
    public RifleAmmo() {
        this.name = "Rifle Ammo";
        this.description = "Rifle-caliber ammunition for use in rifles.";
    }

    @Override
    public void use(Entity user) {
        //do nothing - can't 'use' ammo directly
    }
}

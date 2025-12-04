package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class PistolAmmo extends AmmoItem{
    PistolAmmo() {
        this.name = "Pistol Ammo";
        this.description = "Standard pistol-caliber ammunition for use in handguns and submachine guns.";
        this.quantity = 30;
        this.ammoType = AmmoType.PISTOL;
    }

    @Override
    public void use(Entity user) {
        //do nothing
    }
}

package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class HazmatGear extends GearItem {
    public HazmatGear() {
        this.name = "Hazmat Suit";
        this.description = "Useful in radioactive environments.";
    }
    @Override
    public void use(Entity user) {
        //apply effects -> -5% speed, +30% damage resist
    }
}

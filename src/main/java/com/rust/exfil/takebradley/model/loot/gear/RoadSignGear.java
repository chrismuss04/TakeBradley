package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class RoadSignGear extends GearItem {
    RoadSignGear() {
        this.name = "Road Sign Armor";
        this.description = "Lightweight metal armor, offers decent protection.";
    }

    @Override
    public void use(Entity user) {
        //apply effects -> -15% speed, 50% damage resist
    }
}

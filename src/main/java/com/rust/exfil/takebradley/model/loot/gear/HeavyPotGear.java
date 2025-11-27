package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class HeavyPotGear extends GearItem {
    HeavyPotGear() {
        this.name = "Heavy Pot";
        this.description = "Thick and sturdy, offers excellent protection at cost of speed.";
    }

    @Override
    public void use(Entity user) {
        //apply effects -> -50% speed, 70% damage resist
    }
}

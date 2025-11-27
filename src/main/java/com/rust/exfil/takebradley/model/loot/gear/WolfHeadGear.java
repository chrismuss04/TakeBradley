package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class WolfHeadGear extends GearItem {
    WolfHeadGear() {
         this.name = "WolfHead Armor";
         this.description = description;
   }

    @Override
    public void use(Entity user) {
       //apply effects to user -> -5% speed, 40% damage resist
    }
}

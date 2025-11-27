package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.buff.BuffFactory;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

import static com.rust.exfil.takebradley.model.buff.BuffType.*;

public class HazmatGear extends GearItem {
    HazmatGear() {
        this.name = "Hazmat Suit";
        this.description = "Hazmat suit, provides minmal damage resistance";
        addBuff(BuffFactory.create(DAMAGE_RESIST, 0.20));
        addBuff(BuffFactory.create(MOVEMENT_SPEED, -0.10));
    }
    @Override
    public void use(Entity user) {
        //apply effects -> -5% speed, +30% damage resist
        user.getInventory().setEquippedGear(this);
    }
}

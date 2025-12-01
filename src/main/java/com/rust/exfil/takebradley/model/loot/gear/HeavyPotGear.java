package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.buff.BuffFactory;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import static com.rust.exfil.takebradley.model.buff.BuffType.*;

public class HeavyPotGear extends GearItem {
    HeavyPotGear() {
        this.name = "Heavy Pot";
        this.description = "Thick and sturdy, offers excellent protection at cost of speed.";
        this.gearType = GearType.HEAVYPOT;
        addBuff(BuffFactory.create(DAMAGE_RESIST, 0.70));
        addBuff(BuffFactory.create(MOVEMENT_SPEED, -0.50));
    }

    @Override
    public void use(Entity user) {
        user.getInventory().setEquippedGear(this);
    }
}

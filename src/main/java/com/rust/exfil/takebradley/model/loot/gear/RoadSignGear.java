package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.buff.BuffFactory;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import static com.rust.exfil.takebradley.model.buff.BuffType.*;

public class RoadSignGear extends GearItem {
    RoadSignGear() {
        this.name = "Road Sign Armor";
        this.description = "Lightweight metal armor, offers decent protection.";
        this.gearType = GearType.ROADSIGN;
        addBuff(BuffFactory.create(DAMAGE_RESIST, 0.50));
        addBuff(BuffFactory.create(MOVEMENT_SPEED, -0.15));
    }

    @Override
    public void use(Entity user) {
        // Note: Gear equipping is handled by Inventory.equipGearFromSlot() to avoid duplication
        // This method should not be called directly
        user.getInventory().setEquippedGear(this);
    }
}

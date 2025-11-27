package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.buff.BuffFactory;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import static com.rust.exfil.takebradley.model.buff.BuffType.*;

public class WolfHeadGear extends GearItem {
    WolfHeadGear() {
         this.name = "WolfHead Armor";
         this.description = "Wolf hide armor, offers moderate protection";
         addBuff(BuffFactory.create(DAMAGE_RESIST, 0.40));
            addBuff(BuffFactory.create(MOVEMENT_SPEED, -0.05));
   }

    @Override
    public void use(Entity user) {
       user.getInventory().setEquippedGear(this);
    }
}

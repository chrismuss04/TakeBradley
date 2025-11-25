package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;

public abstract class GearItem implements LootItem {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void use(Entity user) {

    }
}

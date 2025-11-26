package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.loot.LootItem;

public abstract class GearItem implements LootItem {
    String name;
    String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

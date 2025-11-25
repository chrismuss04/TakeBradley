package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;

public abstract class AmmoItem implements LootItem {
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

    @Override
    public void use(Entity user) {

    }
}

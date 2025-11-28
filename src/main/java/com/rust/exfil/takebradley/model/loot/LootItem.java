package com.rust.exfil.takebradley.model.loot;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public interface LootItem {
    String getName();
    String getDescription();


    void use(Entity user);
}

package com.rust.exfil.takebradley.model.loot.medical;

import com.rust.exfil.takebradley.model.loot.LootItem;

public abstract class MedicalItem implements LootItem {
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

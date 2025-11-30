package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.loot.LootItem;

public abstract class AmmoItem implements LootItem {
    String name;
    String description;
    int quantity;
    AmmoType ammoType;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }
}

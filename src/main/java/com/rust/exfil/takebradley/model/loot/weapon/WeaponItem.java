package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.loot.LootItem;

public abstract class WeaponItem implements LootItem {
    String name;
    String description;
    int magazineSize;
    int damage;

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getMagazineSize() {
        return magazineSize;
    }
    public int getDamage() {
        return damage;
    }
    public abstract void reload();
}

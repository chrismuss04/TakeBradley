package com.rust.exfil.takebradley.model.inventory;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import java.util.List;

public class Stash extends Inventory {
    private final static int DEFAULT_STASH_SIZE = 100;

    public Stash(Entity owner) {
        super(DEFAULT_STASH_SIZE, owner);
    }

    public void depositAll(List<LootItem> raidLoot) {
        for (LootItem item : raidLoot) {
            this.addItem(item);
        }
    }
}

package com.rust.exfil.takebradley.model.inventory;

import com.rust.exfil.takebradley.model.loot.LootItem;
import java.util.List;

public class Stash extends Inventory {
    private final static int DEFAULT_STASH_SIZE = 100;

    public Stash() {
        super(DEFAULT_STASH_SIZE);
    }

    public void depositAll(List<LootItem> raidLoot) {
        for (LootItem item : raidLoot) {
            this.addItem(item);
        }
    }
}

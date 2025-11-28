package com.rust.exfil.takebradley.systems.event;

import com.rust.exfil.takebradley.model.loot.LootItem;

public class LootSpawnEvent implements GameEvent {
    private final LootItem lootItem;
    private final double x;
    private final double y;
    private final long timestamp;

    public LootSpawnEvent(LootItem lootItem, double x, double y) {
        this.lootItem = lootItem;
        this.x = x;
        this.y = y;
        this.timestamp = System.currentTimeMillis();
    }

    public LootItem getLootItem() {
        return lootItem;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.LOOT_SPAWN;
    }
}

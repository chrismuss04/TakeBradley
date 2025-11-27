package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class EntityFactory {
    public static Entity createEntity(EntityType type, String name, int x, int y) {
        switch (type) {
            case PLAYER:
                return new Player(name, x, y);
            case NPC:
                return new NpcPlayer(name, x, y);
            case SCIENTIST:
                return new Scientist(name, x, y);
            case BRADLEY_APC:
                return new BradleyAPC(name, x, y);
            case LOOT_CRATE:
                return new LootCrate(name, x, y);
            case ELITE_CRATE:
                return new EliteCrate(name, x, y);
            default:
                throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}

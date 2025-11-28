package com.rust.exfil.takebradley.systems.event;

import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.Projectile;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;

public class GameEventFactory {
    public static EntityDeathEvent createEntityDeathEvent(Entity entity) {
        return new EntityDeathEvent(entity);
    }

    public static LootSpawnEvent createLootSpawnEvent(LootItem lootItem, double x, double y) {
        return new LootSpawnEvent(lootItem, x, y);
    }

    public static ExtractionEvent createExtractionEvent(Player player) {
        return new ExtractionEvent(player);
    }

    public static ProjectileCreatedEvent createProjectileCreatedEvent(Projectile projectile) {
        return new ProjectileCreatedEvent(projectile);
    }

}

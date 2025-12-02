package com.rust.exfil.takebradley.systems.event;

import com.rust.exfil.takebradley.model.entity.Projectile;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class ProjectileHitEvent implements GameEvent {
    private final Projectile projectile;
    private final Entity target;
    private final long timestamp;

    public ProjectileHitEvent(Projectile projectile, Entity target) {
        this.projectile = projectile;
        this.target = target;
        this.timestamp = System.currentTimeMillis();
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public Entity getTarget() {
        return target;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.PROJECTILE_HIT;
    }
}

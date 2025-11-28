package com.rust.exfil.takebradley.systems.event;

import com.rust.exfil.takebradley.model.entity.Projectile;

public class ProjectileCreatedEvent implements GameEvent {
    private final Projectile projectile;
    private final long timestamp;

    public ProjectileCreatedEvent(Projectile projectile) {
        this.projectile = projectile;
        this.timestamp = System.currentTimeMillis();
    }

    public Projectile getProjectile() {
        return projectile;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.PROJECTILE_CREATED;
    }
}

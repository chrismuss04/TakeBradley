package com.rust.exfil.takebradley.systems.event;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class EntityDeathEvent implements GameEvent {
    private final Entity entity;
    private final long timestamp;

    public EntityDeathEvent(Entity entity) {
        this.entity = entity;
        this.timestamp = System.currentTimeMillis();
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.ENTITY_DEATH;
    }
}

package com.rust.exfil.takebradley.systems.event;

// TODO: Uncomment when Projectile class is implemented in task 2
// import com.rust.exfil.takebradley.model.entity.Projectile;

public class ProjectileCreatedEvent implements GameEvent {
    // TODO: Uncomment when Projectile class is implemented
    // private final Projectile projectile;
    private final long timestamp;

    public ProjectileCreatedEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    // TODO: Uncomment when Projectile class is implemented
    // public ProjectileCreatedEvent(Projectile projectile) {
    //     this.projectile = projectile;
    //     this.timestamp = System.currentTimeMillis();
    // }
    //
    // public Projectile getProjectile() {
    //     return projectile;
    // }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.PROJECTILE_CREATED;
    }
}

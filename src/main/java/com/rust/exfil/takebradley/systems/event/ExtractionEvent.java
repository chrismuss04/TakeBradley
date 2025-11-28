package com.rust.exfil.takebradley.systems.event;

import com.rust.exfil.takebradley.model.entity.Player;

public class ExtractionEvent implements GameEvent {
    private final Player player;
    private final long timestamp;

    public ExtractionEvent(Player player) {
        this.player = player;
        this.timestamp = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public GameEventType getEventType() {
        return GameEventType.EXTRACTION;
    }
}

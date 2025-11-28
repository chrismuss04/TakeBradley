package com.rust.exfil.takebradley.systems.event;

public interface GameEvent {
    long getTimestamp();
    GameEventType getEventType();
}

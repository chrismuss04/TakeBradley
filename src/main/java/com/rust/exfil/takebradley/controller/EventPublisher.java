package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.systems.event.GameEvent;
import com.rust.exfil.takebradley.systems.event.EventObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPublisher {
    private static EventPublisher instance;
    private Map<Class<? extends GameEvent>, List<EventObserver>> observers;

    private EventPublisher() {
        observers = new HashMap<>();
    }

    public static EventPublisher getInstance() {
        if (instance == null) {
            instance = new EventPublisher();
        }
        return instance;
    }

    public void subscribe(Class<? extends GameEvent> eventType, EventObserver observer) {
        observers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(observer);
    }

    public void publish(GameEvent event) {
        List<EventObserver> eventObservers = observers.get(event.getClass());
        if (eventObservers != null) {
            for (EventObserver observer : eventObservers) {
                observer.onEvent(event);
            }
        }
    }
}

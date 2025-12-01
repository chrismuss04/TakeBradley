package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.systems.event.EventObserver;
import com.rust.exfil.takebradley.systems.event.GameEvent;
import com.rust.exfil.takebradley.systems.event.ProjectileCreatedEvent;
import javafx.animation.AnimationTimer;

public class GameController implements EventObserver {
    private final GameWorld gameWorld;
    private final SpawnController spawnController;
    private final ExfilController exfilController;
    private AnimationTimer gameLoop;
    private long lastUpdate = 0;

    public GameController(GameWorld gameWorld, SpawnController spawnController, ExfilController exfilController) {
        this.gameWorld = gameWorld;
        this.spawnController = spawnController;
        this.exfilController = exfilController;
        
        // subscribe to ProjectileCreatedEvent
        EventPublisher.getInstance().subscribe(ProjectileCreatedEvent.class, this);
    }

    // start a 'raid'
    public void startRaid() {
        lastUpdate = 0;
        
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                
                // calculate delta time in seconds
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                
                // update game state
                gameWorld.updateAll(deltaTime);
            }
        };
        
        gameLoop.start();
    }

    // end raid and cleanup
    public void endRaid() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        lastUpdate = 0;
    }

    // handle game events
    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof ProjectileCreatedEvent) {
            ProjectileCreatedEvent projectileEvent = (ProjectileCreatedEvent) event;
            gameWorld.addEntity(projectileEvent.getProjectile());
        }
    }

    // Getters for controllers
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public SpawnController getSpawnController() {
        return spawnController;
    }

    public ExfilController getExfilController() {
        return exfilController;
    }
}

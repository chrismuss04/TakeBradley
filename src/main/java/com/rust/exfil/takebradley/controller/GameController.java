package com.rust.exfil.takebradley.controller;


import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.BradleyAPC;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.systems.event.EntityDeathEvent;
import com.rust.exfil.takebradley.systems.event.EventObserver;
import com.rust.exfil.takebradley.systems.event.GameEvent;
import com.rust.exfil.takebradley.systems.event.ProjectileCreatedEvent;
import com.rust.exfil.takebradley.systems.event.ProjectileHitEvent;
import com.rust.exfil.takebradley.view.GameRenderer;
import javafx.animation.AnimationTimer;

public class GameController implements EventObserver {
    private final GameWorld gameWorld;
    private final SpawnController spawnController;
    private final ExfilController exfilController;
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;
    private AnimationTimer gameLoop;
    private long lastUpdate = 0;

    public GameController(GameWorld gameWorld, SpawnController spawnController, ExfilController exfilController) {
        this.gameWorld = gameWorld;
        this.spawnController = spawnController;
        this.exfilController = exfilController;
        
        // subscribe to events
        EventPublisher.getInstance().subscribe(ProjectileCreatedEvent.class, this);
        EventPublisher.getInstance().subscribe(com.rust.exfil.takebradley.systems.event.ProjectileHitEvent.class, this);
        EventPublisher.getInstance().subscribe(com.rust.exfil.takebradley.systems.event.EntityDeathEvent.class, this);
    }
    
    // Set the game renderer (called from Main after initialization)
    public void setGameRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }
    
    // Set the input handler (called from Main after initialization)
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
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
                
                // handle input
                if (inputHandler != null) {
                    inputHandler.update(deltaTime);
                }
                
                // update game state
                gameWorld.updateAll(deltaTime);
                
                // render game state
                if (gameRenderer != null) {
                    gameRenderer.render(gameWorld, gameWorld.getPlayer());
                }
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
        } else if (event instanceof ProjectileHitEvent) {
            gameRenderer.getAudioManager().playHitSound();               
        } else if (event instanceof EntityDeathEvent) {
            EntityDeathEvent deathEvent = 
                (EntityDeathEvent) event;
            
            Entity deadEntity = deathEvent.getEntity();
            double x = deadEntity.getX();
            double y = deadEntity.getY();
            
            // if Bradley died, spawn 3 elite crates at its position
            if (deadEntity instanceof BradleyAPC) {
                // spawn 3 elite crates in a small area around Bradley
                spawnController.spawnEliteCrate("Bradley Loot 1", x - 30, y);
                spawnController.spawnEliteCrate("Bradley Loot 2", x + 30, y);
                spawnController.spawnEliteCrate("Bradley Loot 3", x, y + 30);
            } 
            // For other combatants (NPCs, Scientists, Players), drop their inventory as a loot crate
            else if (deadEntity instanceof Combatant) {
                // check if entity has inventory with items
                if (deadEntity.getInventory() != null && !deadEntity.getInventory().isEmpty()) {
                    // spawn a loot crate with the dead entity's inventory
                    String crateName = deadEntity.getName() + "'s Loot";
                    spawnController.spawnLootCrateWithInventory(crateName, x, y, deadEntity.getInventory());
                }
            }
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

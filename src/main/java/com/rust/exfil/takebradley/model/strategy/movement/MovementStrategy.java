package com.rust.exfil.takebradley.model.strategy.movement;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

// for ai movement strategy
public interface MovementStrategy {
    /**
     * Execute movement logic for an entity
     * @param self The entity executing the strategy
     * @param world The game world for spatial queries
     * @param deltaTime Time elapsed since last update
     */
    void execute(Entity self, GameWorld world, double deltaTime);
}

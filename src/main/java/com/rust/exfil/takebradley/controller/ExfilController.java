package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.systems.event.ExtractionEvent;

public class ExfilController {
    private final GameWorld gameWorld;

    public ExfilController(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    /**
     * Check if player can extract (is in extraction zone and alive)
     */
    public boolean canExtract(Player player) {
        if (!player.isAlive()) {
            return false;
        }
        return gameWorld.isInExtractionZone(player);
    }

    /**
     * Execute extraction - transfer inventory to stash
     */
    public boolean extract(Player player) {
        if (!canExtract(player)) {
            return false;
        }
        
        // Transfer inventory to stash
        player.extract();
        
        // Publish extraction event
        EventPublisher.getInstance().publish(new ExtractionEvent(player));
        
        // Note: Stash serialization handled in stash-serialize spec
        
        return true;
    }
}

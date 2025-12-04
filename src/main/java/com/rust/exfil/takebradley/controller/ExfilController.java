package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.systems.event.ExtractionEvent;
import com.rust.exfil.takebradley.systems.serialization.StashSerializer;

public class ExfilController {
    private final GameWorld gameWorld;

    public ExfilController(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    // check if player is in extraction zone for extract logic
    public boolean canExtract(Player player) {
        if (!player.isAlive()) {
            return false;
        }
        return gameWorld.isInExtractionZone(player);
    }

    // extract from game and save items to stash
    public boolean extract(Player player) {
        if (!canExtract(player)) {
            return false;
        }
        
        // transfer inventory to stash
        player.extract();
        String stashFilePath = "saves/player_stash.json";
        boolean saved = StashSerializer.serialize(player.getStash(), stashFilePath);
        
        if (saved) {
            System.out.println("Stash saved successfully to " + stashFilePath);
        } else {
            System.err.println("Failed to save stash");
        }
        
        // publish extraction event
        EventPublisher.getInstance().publish(new ExtractionEvent(player));
        
        return true;
    }
}

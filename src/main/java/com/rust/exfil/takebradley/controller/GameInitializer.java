package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.map.*;
import com.rust.exfil.takebradley.systems.MapLoader;

import java.util.Random;

public class GameInitializer {
    private static final Random random = new Random();
    
    public static GameController initializeGame() {
        // load game map from MapLoader
        GameMap gameMap = MapLoader.loadMap();
        
    
        // create game world with that map
        GameWorld gameWorld = new GameWorld(gameMap);
        
        // initialize controllers
        SpawnController spawnController = new SpawnController(gameWorld);
        ExfilController exfilController = new ExfilController(gameWorld);
        GameController gameController = new GameController(gameWorld, spawnController, exfilController);
        
        // spawn entities - pass gameMap directly
        spawnEntities(spawnController, gameMap);
        
        return gameController;
    }
    
    // spawn all entites 
    private static void spawnEntities(SpawnController spawnController, GameMap map) {
        
        // spawn player at starting position (in outskirts zone)
        spawnController.spawnPlayer(100, 600, null);
        
        // spawn NPCs in outskirts (3 per zone)
        Zone[] outskirts = map.getZones().stream()
            .filter(z -> z.getZoneType() == ZoneType.OUTSKIRTS)
            .toArray(Zone[]::new);
        
        GameWorld gameWorld = spawnController.getGameWorld();
        
        int npcCount = 1;
        for (Zone outskirt : outskirts) {
            for (int i = 0; i < 3; i++) {
                double[] pos = getRandomPositionInZoneWithoutOverlap(outskirt, gameWorld, 32, 50);
                spawnController.spawnNPC("NPC " + npcCount, pos[0], pos[1]);
                npcCount++;
            }
        }
        
        // spawn Scientists in loot rooms (2 per room)
        Zone[] lootRooms = map.getZones().stream()
            .filter(z -> z.getZoneType() == ZoneType.LOOT_ROOM)
            .toArray(Zone[]::new);
        
        int scientistCount = 1;
        for (Zone lootRoom : lootRooms) {
            for (int j = 0; j < 2; j++) {
                double[] pos = getRandomPositionInZoneWithoutOverlap(lootRoom, gameWorld, 32, 50);
                spawnController.spawnScientist("Scientist " + scientistCount, pos[0], pos[1]);
                scientistCount++;
            }
        }
        
        // spawn Bradley in Bradley zone
        Zone bradleyZone = map.getZones().stream()
            .filter(z -> z.getZoneType() == ZoneType.BRADLEY_ZONE)
            .findFirst()
            .orElse(null);
        
        if (bradleyZone != null) {
            double[] pos = getRandomPositionInZoneWithoutOverlap(bradleyZone, gameWorld, 64, 50);
            spawnController.spawnBradley("Bradley APC", pos[0], pos[1]);
        }
        
        // spawn 3 crates per loot room (10% chance of elite)
        int crateCount = 1;
        for (Zone lootRoom : lootRooms) {
            for (int j = 0; j < 3; j++) {
                double[] pos = getRandomPositionInZoneWithoutOverlap(lootRoom, gameWorld, 16, 50);
                
                if (random.nextDouble() < 0.10) {
                    spawnController.spawnEliteCrate("Elite Crate " + crateCount, pos[0], pos[1]);
                } else {
                    spawnController.spawnLootCrate("Crate " + crateCount, pos[0], pos[1]);
                }
                crateCount++;
            }
        }
    }
    
    // get a random position in a zone that doesn't overlap with existing entities
    private static double[] getRandomPositionInZoneWithoutOverlap(Zone zone, GameWorld gameWorld, double entitySize, int maxAttempts) {
        double padding = 20;
        double minDistance = entitySize + 10;
        
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            double x = zone.getX() + padding + random.nextDouble() * (zone.getWidth() - 2 * padding);
            double y = zone.getY() + padding + random.nextDouble() * (zone.getHeight() - 2 * padding);
            
            // check if this position overlaps with any existing entity
            boolean overlaps = false;
            for (com.rust.exfil.takebradley.model.entity.interfaces.Entity entity : gameWorld.getEntities()) {
                double dx = entity.getX() - x;
                double dy = entity.getY() - y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance < minDistance) {
                    overlaps = true;
                    break;
                }
            }
            
            if (!overlaps) {
                return new double[]{x, y};
            }
        }
        
        // ok with spawn collisions after max attempts
        double x = zone.getX() + padding + random.nextDouble() * (zone.getWidth() - 2 * padding);
        double y = zone.getY() + padding + random.nextDouble() * (zone.getHeight() - 2 * padding);
        return new double[]{x, y};
    }

}

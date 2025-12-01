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
        
        // Spawn player at starting position (fixed for now)
        spawnController.spawnPlayer(10, 10, null);
        
        // Spawn NPCs in outskirts (3 per zone)
        Zone[] outskirts = map.getZones().stream()
            .filter(z -> z.getZoneType() == ZoneType.OUTSKIRTS)
            .toArray(Zone[]::new);
        
        int npcCount = 1;
        for (Zone outskirt : outskirts) {
            for (int i = 0; i < 3; i++) {
                double[] pos = getRandomPositionInZone(outskirt);
                spawnController.spawnNPC("NPC " + npcCount, pos[0], pos[1]);
                npcCount++;
            }
        }
        
        // Spawn Scientists in loot rooms (2 per room)
        Zone[] lootRooms = map.getZones().stream()
            .filter(z -> z.getZoneType() == ZoneType.LOOT_ROOM)
            .toArray(Zone[]::new);
        
        int scientistCount = 1;
        for (Zone lootRoom : lootRooms) {
            for (int j = 0; j < 2; j++) {
                double[] pos = getRandomPositionInZone(lootRoom);
                spawnController.spawnScientist("Scientist " + scientistCount, pos[0], pos[1]);
                scientistCount++;
            }
        }
        
        // Spawn Bradley in Bradley zone
        Zone bradleyZone = map.getZones().stream()
            .filter(z -> z.getZoneType() == ZoneType.BRADLEY_ZONE)
            .findFirst()
            .orElse(null);
        
        if (bradleyZone != null) {
            double[] pos = getRandomPositionInZone(bradleyZone);
            spawnController.spawnBradley("Bradley APC", pos[0], pos[1]);
        }
        
        // Spawn 3 crates per loot room (10% chance of elite)
        int crateCount = 1;
        for (Zone lootRoom : lootRooms) {
            for (int j = 0; j < 3; j++) {
                double[] pos = getRandomPositionInZone(lootRoom);
                
                // 10% chance for elite crate
                if (random.nextDouble() < 0.10) {
                    spawnController.spawnEliteCrate("Elite Crate " + crateCount, pos[0], pos[1]);
                } else {
                    spawnController.spawnLootCrate("Crate " + crateCount, pos[0], pos[1]);
                }
                crateCount++;
            }
        }
    }
    
    // get a random position in a zone
    private static double[] getRandomPositionInZone(Zone zone) {
        // Add padding to avoid spawning too close to walls
        double padding = 20;
        
        double x = zone.getX() + padding + random.nextDouble() * (zone.getWidth() - 2 * padding);
        double y = zone.getY() + padding + random.nextDouble() * (zone.getHeight() - 2 * padding);
        
        return new double[]{x, y};
    }

}

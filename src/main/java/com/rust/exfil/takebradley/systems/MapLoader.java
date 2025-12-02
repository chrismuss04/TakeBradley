package com.rust.exfil.takebradley.systems;

import com.rust.exfil.takebradley.model.map.GameMap;
import com.rust.exfil.takebradley.model.map.Wall;
import com.rust.exfil.takebradley.model.map.ZoneFactory;
import com.rust.exfil.takebradley.model.map.ZoneType;

public class MapLoader {
    private static final int MAP_SIZE = 1500;  // Map dimensions
    private static final int OUTSKIRTS_WIDTH = 150;  // Width of outskirts border
    private static final int WALL_THICKNESS = 10;
    private static final int DOOR_SIZE = 60;
    
    public static GameMap loadMap() {
        GameMap map = new GameMap();
        
        // === ZONES ===
        // Map is 1500x1500 pixels
        
        // Bradley zone - CENTER of map where bradley spawns and roams
        int bradleySize = 400;
        int bradleyX = (MAP_SIZE - bradleySize) / 2;  // 550
        int bradleyY = (MAP_SIZE - bradleySize) / 2;  // 550
        map.addZone(ZoneFactory.createZone(ZoneType.BRADLEY_ZONE, bradleyX, bradleyY, bradleySize, bradleySize));
        
        // Loot rooms - spread around the Bradley zone (4 rooms, one on each side)
        int lootRoomSize = 200;
        int lootRoomOffset = 250;  // Distance from edge to loot room
        
        // North loot room
        int northX = (MAP_SIZE - lootRoomSize) / 2;  // 650
        int northY = lootRoomOffset;  // 250
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, northX, northY, lootRoomSize, lootRoomSize));
        
        // East loot room
        int eastX = MAP_SIZE - lootRoomOffset - lootRoomSize;  // 1050
        int eastY = (MAP_SIZE - lootRoomSize) / 2;  // 650
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, eastX, eastY, lootRoomSize, lootRoomSize));
        
        // South loot room
        int southX = (MAP_SIZE - lootRoomSize) / 2;  // 650
        int southY = MAP_SIZE - lootRoomOffset - lootRoomSize;  // 1050
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, southX, southY, lootRoomSize, lootRoomSize));
        
        // West loot room
        int westX = lootRoomOffset;  // 250
        int westY = (MAP_SIZE - lootRoomSize) / 2;  // 650
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, westX, westY, lootRoomSize, lootRoomSize));
        
        // Northeast loot room
        int neX = MAP_SIZE - lootRoomOffset - lootRoomSize;  // 1050
        int neY = lootRoomOffset;  // 250
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, neX, neY, lootRoomSize, lootRoomSize));
        
        // Southwest loot room
        int swX = lootRoomOffset;  // 250
        int swY = MAP_SIZE - lootRoomOffset - lootRoomSize;  // 1050
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, swX, swY, lootRoomSize, lootRoomSize));
        
        // Extraction zones - corners of map
        int extractSize = 150;
        int extractPadding = 50;
        map.addZone(ZoneFactory.createZone(ZoneType.EXTRACT_ZONE, extractPadding, extractPadding, extractSize, extractSize));
        map.addZone(ZoneFactory.createZone(ZoneType.EXTRACT_ZONE, 
            MAP_SIZE - extractPadding - extractSize, MAP_SIZE - extractPadding - extractSize, extractSize, extractSize));
        
        // Outskirt zones - where player and NPCs spawn (all edges of the map)
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 0, 0, MAP_SIZE, OUTSKIRTS_WIDTH));     // Top edge
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 0, MAP_SIZE - OUTSKIRTS_WIDTH, MAP_SIZE, OUTSKIRTS_WIDTH));   // Bottom edge
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 0, OUTSKIRTS_WIDTH, OUTSKIRTS_WIDTH, MAP_SIZE - 2 * OUTSKIRTS_WIDTH));    // Left edge
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, MAP_SIZE - OUTSKIRTS_WIDTH, OUTSKIRTS_WIDTH, OUTSKIRTS_WIDTH, MAP_SIZE - 2 * OUTSKIRTS_WIDTH));  // Right edge

        // === WALLS ===
        
        // North Loot Room - opening on SOUTH side (facing Bradley)
        map.addWall(new Wall(northX, northY, lootRoomSize, WALL_THICKNESS));  // North wall
        map.addWall(new Wall(northX, northY, WALL_THICKNESS, lootRoomSize));  // West wall
        map.addWall(new Wall(northX + lootRoomSize - WALL_THICKNESS, northY, WALL_THICKNESS, lootRoomSize));  // East wall
        int doorOffset = (lootRoomSize - DOOR_SIZE) / 2;
        map.addWall(new Wall(northX, northY + lootRoomSize - WALL_THICKNESS, doorOffset, WALL_THICKNESS));   // South wall - left
        map.addWall(new Wall(northX + doorOffset, northY + lootRoomSize - WALL_THICKNESS, DOOR_SIZE, WALL_THICKNESS, true));  // South opening
        map.addWall(new Wall(northX + doorOffset + DOOR_SIZE, northY + lootRoomSize - WALL_THICKNESS, doorOffset, WALL_THICKNESS));   // South wall - right
        
        // East Loot Room - opening on WEST side (facing Bradley)
        map.addWall(new Wall(eastX, eastY, lootRoomSize, WALL_THICKNESS));  // North wall
        map.addWall(new Wall(eastX, eastY + lootRoomSize - WALL_THICKNESS, lootRoomSize, WALL_THICKNESS));  // South wall
        map.addWall(new Wall(eastX + lootRoomSize - WALL_THICKNESS, eastY, WALL_THICKNESS, lootRoomSize));  // East wall
        map.addWall(new Wall(eastX, eastY, WALL_THICKNESS, doorOffset));   // West wall - top
        map.addWall(new Wall(eastX, eastY + doorOffset, WALL_THICKNESS, DOOR_SIZE, true));  // West opening
        map.addWall(new Wall(eastX, eastY + doorOffset + DOOR_SIZE, WALL_THICKNESS, doorOffset));   // West wall - bottom
        
        // South Loot Room - opening on NORTH side (facing Bradley)
        map.addWall(new Wall(southX, southY + lootRoomSize - WALL_THICKNESS, lootRoomSize, WALL_THICKNESS));  // South wall
        map.addWall(new Wall(southX, southY, WALL_THICKNESS, lootRoomSize));  // West wall
        map.addWall(new Wall(southX + lootRoomSize - WALL_THICKNESS, southY, WALL_THICKNESS, lootRoomSize));  // East wall
        map.addWall(new Wall(southX, southY, doorOffset, WALL_THICKNESS));   // North wall - left
        map.addWall(new Wall(southX + doorOffset, southY, DOOR_SIZE, WALL_THICKNESS, true));  // North opening
        map.addWall(new Wall(southX + doorOffset + DOOR_SIZE, southY, doorOffset, WALL_THICKNESS));   // North wall - right
        
        // West Loot Room - opening on EAST side (facing Bradley)
        map.addWall(new Wall(westX, westY, lootRoomSize, WALL_THICKNESS));  // North wall
        map.addWall(new Wall(westX, westY + lootRoomSize - WALL_THICKNESS, lootRoomSize, WALL_THICKNESS));  // South wall
        map.addWall(new Wall(westX, westY, WALL_THICKNESS, lootRoomSize));  // West wall
        map.addWall(new Wall(westX + lootRoomSize - WALL_THICKNESS, westY, WALL_THICKNESS, doorOffset));   // East wall - top
        map.addWall(new Wall(westX + lootRoomSize - WALL_THICKNESS, westY + doorOffset, WALL_THICKNESS, DOOR_SIZE, true));  // East opening
        map.addWall(new Wall(westX + lootRoomSize - WALL_THICKNESS, westY + doorOffset + DOOR_SIZE, WALL_THICKNESS, doorOffset));   // East wall - bottom
        
        // Northeast Loot Room - opening on SOUTH side (facing Bradley)
        map.addWall(new Wall(neX, neY, lootRoomSize, WALL_THICKNESS));  // North wall
        map.addWall(new Wall(neX, neY, WALL_THICKNESS, lootRoomSize));  // West wall
        map.addWall(new Wall(neX + lootRoomSize - WALL_THICKNESS, neY, WALL_THICKNESS, lootRoomSize));  // East wall
        map.addWall(new Wall(neX, neY + lootRoomSize - WALL_THICKNESS, doorOffset, WALL_THICKNESS));   // South wall - left
        map.addWall(new Wall(neX + doorOffset, neY + lootRoomSize - WALL_THICKNESS, DOOR_SIZE, WALL_THICKNESS, true));  // South opening
        map.addWall(new Wall(neX + doorOffset + DOOR_SIZE, neY + lootRoomSize - WALL_THICKNESS, doorOffset, WALL_THICKNESS));   // South wall - right
        
        // Southwest Loot Room - opening on NORTH side (facing Bradley)
        map.addWall(new Wall(swX, swY, WALL_THICKNESS, lootRoomSize));  // West wall
        map.addWall(new Wall(swX + lootRoomSize - WALL_THICKNESS, swY, WALL_THICKNESS, lootRoomSize));  // East wall
        map.addWall(new Wall(swX, swY + lootRoomSize - WALL_THICKNESS, lootRoomSize, WALL_THICKNESS));  // South wall
        map.addWall(new Wall(swX, swY, doorOffset, WALL_THICKNESS));   // North wall - left
        map.addWall(new Wall(swX + doorOffset, swY, DOOR_SIZE, WALL_THICKNESS, true));  // North opening
        map.addWall(new Wall(swX + doorOffset + DOOR_SIZE, swY, doorOffset, WALL_THICKNESS));   // North wall - right

        // Perimeter walls
        map.addWall(new Wall(0, 0, MAP_SIZE, WALL_THICKNESS));     // North perimeter
        map.addWall(new Wall(0, 0, WALL_THICKNESS, MAP_SIZE));     // West perimeter
        map.addWall(new Wall(MAP_SIZE - WALL_THICKNESS, 0, WALL_THICKNESS, MAP_SIZE));   // East perimeter
        map.addWall(new Wall(0, MAP_SIZE - WALL_THICKNESS, MAP_SIZE, WALL_THICKNESS));   // South perimeter

        return map;
    }
}

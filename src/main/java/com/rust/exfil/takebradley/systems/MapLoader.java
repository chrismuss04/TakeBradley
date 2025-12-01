package com.rust.exfil.takebradley.systems;

import com.rust.exfil.takebradley.model.map.GameMap;
import com.rust.exfil.takebradley.model.map.Wall;
import com.rust.exfil.takebradley.model.map.ZoneFactory;
import com.rust.exfil.takebradley.model.map.ZoneType;

public class MapLoader {
    public static GameMap loadMap() {
        GameMap map = new GameMap();
        
        // === ZONES ===
        // extraction zone - where player can extract
        map.addZone(ZoneFactory.createZone(ZoneType.EXTRACT_ZONE, 0, 20, 10, 10));
        // bradley zone - where bradley apc spawns and roams
        map.addZone(ZoneFactory.createZone(ZoneType.BRADLEY_ZONE, 50, 10, 30, 20));
        // loot room zones - where 2x scientist and loot/elite crates spawn
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 40, 30, 10, 10));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 40, 40, 10, 10));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 50, 30, 10, 10));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 50, 40, 10, 10));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 60, 40, 10, 10));
        // outskirt zones - where player and npc player spawn
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 0, 50, 40, 50));
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 40, 60, 40, 40));
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 80, 40, 20, 60));

        // === WALLS ===
        // Example loot room walls (you can customize these positions)
        // Loot room 1 (40, 30, 10, 10) - walls with opening on south side
        map.addWall(new Wall(40, 30, 10, 1));  // North wall
        map.addWall(new Wall(40, 30, 1, 10));  // West wall
        map.addWall(new Wall(49, 30, 1, 10));  // East wall
        map.addWall(new Wall(40, 39, 3, 1));   // South wall - left part
        map.addWall(new Wall(40, 39, 4, 1, true));  // South wall - opening (entrance)
        map.addWall(new Wall(47, 39, 3, 1));   // South wall - right part

        // perimeter walls:
        map.addWall(new Wall(0, 0, 100, 1));
        map.addWall(new Wall(0, 0, 1, 100));
        map.addWall(new Wall(99, 0, 1, 100));
        map.addWall(new Wall(0, 99, 100, 1));

        return map;
    }
}

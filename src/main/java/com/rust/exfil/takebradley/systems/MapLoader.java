package com.rust.exfil.takebradley.systems;

import com.rust.exfil.takebradley.model.map.GameMap;
import com.rust.exfil.takebradley.model.map.Wall;
import com.rust.exfil.takebradley.model.map.ZoneFactory;
import com.rust.exfil.takebradley.model.map.ZoneType;

public class MapLoader {
    public static GameMap loadMap() {
        GameMap map = new GameMap();
        
        // === ZONES ===
        // Map is 1000x1000 pixels
        
        // extraction zone - where player can extract (larger)
        map.addZone(ZoneFactory.createZone(ZoneType.EXTRACT_ZONE, 0, 200, 150, 150));
        
        // bradley zone - where bradley apc spawns and roams (larger)
        map.addZone(ZoneFactory.createZone(ZoneType.BRADLEY_ZONE, 450, 50, 400, 250));
        
        // loot room zones - where 2x scientist and loot/elite crates spawn (much larger - 150x150)
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 350, 300, 150, 150));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 350, 450, 150, 150));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 500, 300, 150, 150));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 500, 450, 150, 150));
        map.addZone(ZoneFactory.createZone(ZoneType.LOOT_ROOM, 650, 450, 150, 150));
        
        // outskirt zones - where player and npc player spawn (larger)
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 0, 600, 450, 400));
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 450, 650, 400, 350));
        map.addZone(ZoneFactory.createZone(ZoneType.OUTSKIRTS, 850, 400, 150, 600));

        // === WALLS ===
        // Loot room 1 (350, 300, 150, 150) - walls with opening on south side
        map.addWall(new Wall(350, 300, 150, 10));  // North wall
        map.addWall(new Wall(350, 300, 10, 150));  // West wall
        map.addWall(new Wall(490, 300, 10, 150));  // East wall
        map.addWall(new Wall(350, 440, 50, 10));   // South wall - left part
        map.addWall(new Wall(400, 440, 50, 10, true));  // South wall - opening (entrance)
        map.addWall(new Wall(450, 440, 50, 10));   // South wall - right part

        // Perimeter walls
        map.addWall(new Wall(0, 0, 1000, 10));     // North perimeter
        map.addWall(new Wall(0, 0, 10, 1000));     // West perimeter
        map.addWall(new Wall(990, 0, 10, 1000));   // East perimeter
        map.addWall(new Wall(0, 990, 1000, 10));   // South perimeter

        return map;
    }
}

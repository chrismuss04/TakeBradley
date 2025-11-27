package com.rust.exfil.takebradley.systems;

import com.rust.exfil.takebradley.model.map.GameMap;
import com.rust.exfil.takebradley.model.map.ZoneFactory;
import com.rust.exfil.takebradley.model.map.ZoneType;

public class MapLoader {
    public static GameMap loadMap() {
        GameMap map = new GameMap();
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

        return map;
    }
}

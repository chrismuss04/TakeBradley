package com.rust.exfil.takebradley.model.map;

public class ZoneFactory {
    public static Zone createZone(ZoneType type, int x, int y, int width, int height) {
        switch (type) {
            case LOOT_ROOM:
                return new LootRoomZone(x, y, width, height);
            case OUTSKIRTS:
                return new OutskirtsZone(x, y, width, height);
            case EXTRACT_ZONE:
                return new ExtractionZone(x, y, width, height);
            case BRADLEY_ZONE:
                return new BradleyZone(x, y, width, height);
            default:
                throw new IllegalArgumentException("Unknown ZoneType: " + type);
        }
    }
}

package com.rust.exfil.takebradley.model.map;

public class LootRoomZone extends Zone{
    // loot room zone is where scientists and loot crates (normal, elite) can spawn
    LootRoomZone(double x, double y, double width, double height) {
        super(ZoneType.LOOT_ROOM, x, y, width, height);
    }
}

package com.rust.exfil.takebradley.model.map;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private final List<Zone> zones = new ArrayList<>();
    private final List<Wall> walls = new ArrayList<>();

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }

    public List<Zone> getZones() {
        return new ArrayList<>(zones);
    }

    public List<Wall> getWalls() {
        return new ArrayList<>(walls);
    }

    public Zone getZoneAt(double x, double y) {
        for (Zone zone : zones) {
            if (zone.contains(x, y)) {
                return zone;
            }
        }
        return null;
    }

}

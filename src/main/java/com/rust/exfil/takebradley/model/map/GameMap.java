package com.rust.exfil.takebradley.model.map;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private final List<Zone> zones = new ArrayList<>();

    public void addZone(Zone zone) {
        zones.add(zone);
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

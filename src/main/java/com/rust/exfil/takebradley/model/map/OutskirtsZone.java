package com.rust.exfil.takebradley.model.map;

public class OutskirtsZone extends Zone{
    // outskirts zone is where Player and NpcPlayer can spawn
    OutskirtsZone(double x, double y, double width, double height) {
        super(ZoneType.OUTSKIRTS, x, y, width, height);
    }
}

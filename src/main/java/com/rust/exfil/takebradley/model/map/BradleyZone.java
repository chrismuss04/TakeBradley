package com.rust.exfil.takebradley.model.map;

public class BradleyZone extends Zone{
    // bradley zone is where bradley spawns and patrols
    BradleyZone(double x, double y, double width, double height) {
        super(ZoneType.BRADLEY_ZONE, x, y, width, height);
    }
}

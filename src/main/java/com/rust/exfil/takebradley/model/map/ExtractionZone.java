package com.rust.exfil.takebradley.model.map;

public class ExtractionZone extends Zone{
    // extraction zone is where player goes to start extraction timer
    ExtractionZone(double x, double y, double width, double height) {
        super(ZoneType.EXTRACT_ZONE, x, y, width, height);
    }
}

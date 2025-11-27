package com.rust.exfil.takebradley.model.map;

public abstract class Zone {
    ZoneType zoneType;
    double x, y, width, height;

    Zone(ZoneType zoneType, double x, double y, double width, double height) {
        this.zoneType = zoneType;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public ZoneType getZoneType() {
        return zoneType;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    public boolean contains(int px, int py) {
        return px >= x && px < x + width &&
                py >= y && py < y + height;
    }
}

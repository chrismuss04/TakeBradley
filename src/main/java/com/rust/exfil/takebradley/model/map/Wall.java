package com.rust.exfil.takebradley.model.map;

public class Wall {
    private final double x, y;
    private final double width, height;
    private final boolean isOpening;

    public Wall(double x, double y, double width, double height) {
        this(x, y, width, height, false);
    }

    public Wall(double x, double y, double width, double height, boolean isOpening) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isOpening = isOpening;
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

    public boolean isOpening() {
        return isOpening;
    }

    public boolean intersects(double px, double py) {
        if (isOpening) {
            return false; // Openings allow passage
        }

        // check point collision
        return px >= x && px < x + width &&
               py >= y && py < y + height;
    }
    
    // check collision with entity bounds for collision detection
    public boolean intersectsEntity(double entityX, double entityY, double entitySize) {
        if (isOpening) {
            return false; // Openings allow passage
        }
        
        // axis aligned bounding box collision detection
        double entityLeft = entityX - entitySize / 2;
        double entityRight = entityX + entitySize / 2;
        double entityTop = entityY - entitySize / 2;
        double entityBottom = entityY + entitySize / 2;
        
        return entityRight > x && entityLeft < x + width &&
               entityBottom > y && entityTop < y + height;
    }
}

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

        return px >= x && px < x + width &&
               py >= y && py < y + height;
    }
}

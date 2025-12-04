package com.rust.exfil.takebradley.view;

public class Camera {
    private double x, y;                    // camera position (top-left of viewport)
    private final double viewportWidth;     // canvas width
    private final double viewportHeight;    // canvas height
    private final double mapWidth;          // total map width
    private final double mapHeight;         // total map height

    public Camera(double viewportWidth, double viewportHeight, double mapWidth, double mapHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.x = 0;
        this.y = 0;
    }

    // center the camera on (x,y) of player
    public void centerOn(double targetX, double targetY) {
        // calculate camera position to center on target
        x = targetX - (viewportWidth / 2);
        y = targetY - (viewportHeight / 2);

        // clamp to map bounds
        x = Math.max(0, Math.min(x, mapWidth - viewportWidth));
        y = Math.max(0, Math.min(y, mapHeight - viewportHeight));
    }

    // check if entity is within viewport for rendering
    public boolean isVisible(double entityX, double entityY, double entityWidth, double entityHeight) {
        // Check if entity bounds intersect with viewport bounds
        return entityX + entityWidth >= x &&
               entityX <= x + viewportWidth &&
               entityY + entityHeight >= y &&
               entityY <= y + viewportHeight;
    }

    public double getCameraX() {
        return x;
    }

    public double getCameraY() {
        return y;
    }

    public double getViewportWidth() {
        return viewportWidth;
    }

    public double getViewportHeight() {
        return viewportHeight;
    }
}

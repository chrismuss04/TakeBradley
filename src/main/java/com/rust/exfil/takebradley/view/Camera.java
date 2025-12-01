package com.rust.exfil.takebradley.view;

/**
 * Camera system that follows the player and manages viewport
 */
public class Camera {
    private double x, y;                    // Camera position (top-left of viewport)
    private final double viewportWidth;     // Canvas width
    private final double viewportHeight;    // Canvas height
    private final double mapWidth;          // Total map width
    private final double mapHeight;         // Total map height

    public Camera(double viewportWidth, double viewportHeight, double mapWidth, double mapHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Center the camera on a position (typically the player)
     */
    public void centerOn(double targetX, double targetY) {
        // Calculate camera position to center on target
        x = targetX - (viewportWidth / 2);
        y = targetY - (viewportHeight / 2);

        // Clamp to map bounds
        x = Math.max(0, Math.min(x, mapWidth - viewportWidth));
        y = Math.max(0, Math.min(y, mapHeight - viewportHeight));
    }

    /**
     * Check if a position is visible in the viewport
     * Used for culling off-screen entities
     */
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

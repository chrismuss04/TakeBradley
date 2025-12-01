package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class GameRenderer {
    private Canvas canvas;
    private GraphicsContext gc;
    private Camera camera;
    private SpriteManager spriteManager;
    private BackgroundRenderer backgroundRenderer;
    private WallRenderer wallRenderer;
    
    
    public void initialize(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        
        // Initialize camera with viewport and map dimensions
        // Map size is 1000x1000 (from GameInitializer)
        this.camera = new Camera(canvas.getWidth(), canvas.getHeight(), 1000, 1000);
        
        // Initialize sprite manager and load sprites
        this.spriteManager = new SpriteManager();
        this.spriteManager.loadSprites();
        
        // Initialize renderers
        this.backgroundRenderer = new BackgroundRenderer();
        this.wallRenderer = new WallRenderer();
    }
    
    // render game state, called by controller
    public void render(GameWorld world, Player player) {
        if (gc == null) {
            throw new IllegalStateException("GameRenderer not initialized. Call initialize() first.");
        }
        
        // clear canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Update camera to follow player
        camera.centerOn(player.getX(), player.getY());
        
        // Apply camera transformation for world rendering
        gc.save();
        gc.translate(-camera.getCameraX(), -camera.getCameraY());
        
        // Render world layers
        backgroundRenderer.renderBackground(gc, world.getMap());
        backgroundRenderer.renderZones(gc, world.getMap());
        wallRenderer.renderWalls(gc, world.getMap().getWalls());
        
        // TODO: Render entities
        // TODO: Render projectiles
        
        // Remove camera transformation
        gc.restore();
        
        // TODO: Render HUD (fixed screen position)
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    public GraphicsContext getGraphicsContext() {
        return gc;
    }
}

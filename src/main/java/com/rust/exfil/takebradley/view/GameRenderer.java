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
    private EntityRenderer entityRenderer;
    private ProjectileRenderer projectileRenderer;
    private HUDRenderer hudRenderer;
    private AudioManager audioManager;
    
    
    public void initialize(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        
        // Initialize camera with viewport and map dimensions
        // Map size is 1000x1000 (from GameInitializer)
        this.camera = new Camera(canvas.getWidth(), canvas.getHeight(), 1000, 1000);
        
        // Initialize sprite manager and load sprites
        this.spriteManager = new SpriteManager();
        this.spriteManager.loadSprites();
        
        // initialize renderers
        this.backgroundRenderer = new BackgroundRenderer();
        this.wallRenderer = new WallRenderer();
        this.entityRenderer = new EntityRenderer(camera);
        this.projectileRenderer = new ProjectileRenderer();
        this.hudRenderer = new HUDRenderer();
        
        // initialize audio manager and load sounds
        this.audioManager = new AudioManager();
        this.audioManager.loadSounds();
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
        
        // render world layers
        backgroundRenderer.renderBackground(gc, world.getMap());
        backgroundRenderer.renderZones(gc, world.getMap());
        wallRenderer.renderWalls(gc, world.getMap().getWalls());
        entityRenderer.renderEntities(gc, world.getEntities(), spriteManager);
        projectileRenderer.renderProjectiles(gc, world.getEntities(), spriteManager);
        
        
        // Remove camera transformation
        gc.restore();
        
        // render hud in fixed screen position
        hudRenderer.renderHUD(gc, player);
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
    
    public AudioManager getAudioManager() {
        return audioManager;
    }
}

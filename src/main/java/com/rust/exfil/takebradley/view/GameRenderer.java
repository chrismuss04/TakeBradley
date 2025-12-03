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
    private LootUIRenderer lootUIRenderer;
    private com.rust.exfil.takebradley.controller.InputHandler inputHandler;
    
    // Post-raid overlay state
    private boolean showingPostRaid = false;
    private boolean playerExtracted = false; // true = extracted, false = died
    
    
    public void initialize(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        
        // Initialize camera with viewport and map dimensions
        // Map size is 1500x1500
        this.camera = new Camera(canvas.getWidth(), canvas.getHeight(), 1500, 1500);
        
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
        
        // initialize loot UI renderer
        this.lootUIRenderer = new LootUIRenderer();
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
        if (inputHandler != null && inputHandler.isExtracting()) {
            // render HUD with extraction progress
            hudRenderer.renderHUD(gc, player, true, inputHandler.getExtractionProgress());
        } else {
            // render normal HUD
            hudRenderer.renderHUD(gc, player);
        }
        
        // render loot UI overlay if open (on top of everything)
        if (lootUIRenderer.isOpen()) {
            lootUIRenderer.renderLootUI(gc);
        }
        
        // render post-raid overlay if showing (on top of everything)
        if (showingPostRaid) {
            renderPostRaidOverlay(gc);
        }
    }
    
    private void renderPostRaidOverlay(GraphicsContext gc) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        
        // semi-transparent background
        if (playerExtracted) {
            gc.setFill(javafx.scene.paint.Color.rgb(0, 100, 0, 0.9)); // Dark green
        } else {
            gc.setFill(javafx.scene.paint.Color.rgb(100, 0, 0, 0.9)); // Dark red
        }
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // title message
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 48));
        
        String titleText = playerExtracted ? "EXTRACTED!" : "KILLED IN ACTION";
        double titleX = canvasWidth / 2 - (titleText.length() * 48 * 0.6) / 2;
        double titleY = canvasHeight / 2 - 50;
        gc.fillText(titleText, titleX, titleY);
        
        // instruction message
        gc.setFont(javafx.scene.text.Font.font("Arial", 20));
        String instructionText = "Press ENTER to return to start screen";
        double instructionX = canvasWidth / 2 - (instructionText.length() * 20 * 0.6) / 2;
        double instructionY = canvasHeight / 2 + 50;
        gc.fillText(instructionText, instructionX, instructionY);
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
    
    public LootUIRenderer getLootUIRenderer() {
        return lootUIRenderer;
    }
    
    public void setInputHandler(com.rust.exfil.takebradley.controller.InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }
    
    public void showExtractionOverlay() {
        playerExtracted = true;
        showingPostRaid = true;
    }
    
    public void showDeathOverlay() {
        playerExtracted = false;
        showingPostRaid = true;
    }
    
    public void hidePostRaid() {
        showingPostRaid = false;
    }
    
    public boolean isShowingPostRaid() {
        return showingPostRaid;
    }
}

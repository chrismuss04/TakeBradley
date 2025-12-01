package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class GameRenderer {
    private Canvas canvas;
    private GraphicsContext gc;
    
    
    public void initialize(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }
    
    // render game state, called by controller
    public void render(GameWorld world, Player player) {
        if (gc == null) {
            throw new IllegalStateException("GameRenderer not initialized. Call initialize() first.");
        }
        
        // clear canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // TODO: Implement rendering layers
        // 1. Background
        // 2. Zones
        // 3. Extraction zones
        // 4. Walls
        // 5. Entities
        // 6. Projectiles
        // 7. HUD
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    public GraphicsContext getGraphicsContext() {
        return gc;
    }
}

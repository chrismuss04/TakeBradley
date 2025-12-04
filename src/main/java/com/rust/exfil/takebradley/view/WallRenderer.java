package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.map.Wall;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class WallRenderer {
    
    private static final Color WALL_COLOR = Color.rgb(80, 80, 85);        // Gray for solid walls
    private static final Color OPENING_COLOR = Color.rgb(60, 60, 65, 0.5); // Semi-transparent for openings
    private static final Color WALL_STROKE = Color.rgb(50, 50, 55);       // Darker outline
    
    public void renderWalls(GraphicsContext gc, List<Wall> walls) {
        for (Wall wall : walls) {
            if (wall.isOpening()) {
                // render opening as dashed/semi-transparent
                renderOpening(gc, wall);
            } else {
                // render solid wall
                renderSolidWall(gc, wall);
            }
        }
    }
    
    
    private void renderSolidWall(GraphicsContext gc, Wall wall) {
        // fill wall
        gc.setFill(WALL_COLOR);
        gc.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        
        // set outline
        gc.setStroke(WALL_STROKE);
        gc.setLineWidth(1);
        gc.strokeRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
    }
    
    
    private void renderOpening(GraphicsContext gc, Wall wall) {
        // render as semi-transparent to show it's passable
        gc.setFill(OPENING_COLOR);
        gc.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        
        // dashed outline to indicate opening
        gc.setStroke(WALL_STROKE);
        gc.setLineWidth(1);
        gc.setLineDashes(5, 5);
        gc.strokeRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        gc.setLineDashes(null);
    }
}

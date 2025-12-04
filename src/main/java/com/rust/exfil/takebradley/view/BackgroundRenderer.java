package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.map.GameMap;
import com.rust.exfil.takebradley.model.map.Zone;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class BackgroundRenderer {
    
    // placeholder color scheme
    private static final Color BACKGROUND_COLOR = Color.rgb(60, 60, 65);      // Dark gray (main area)
    private static final Color BRADLEY_ZONE_COLOR = Color.rgb(45, 45, 50);   // Darker gray (Bradley pad)
    private static final Color LOOT_ROOM_COLOR = Color.rgb(100, 100, 100);   // Light gray (buildings)
    private static final Color EXTRACTION_ZONE_COLOR = Color.rgb(0, 255, 0, 0.3);  // Semi-transparent green
    
    // render main background
    public void renderBackground(GraphicsContext gc, GameMap map) {
        // Main background - dark gray concrete
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, 1500, 1500);
    }
    
    // render different zones different colors
    public void renderZones(GraphicsContext gc, GameMap map) {
        for (Zone zone : map.getZones()) {
            Color zoneColor = null;
            
            switch(zone.getZoneType()) {
                case BRADLEY_ZONE:
                    zoneColor = BRADLEY_ZONE_COLOR;
                    break;
                case LOOT_ROOM:
                    zoneColor = LOOT_ROOM_COLOR;
                    break;
                case EXTRACT_ZONE:
                    zoneColor = EXTRACTION_ZONE_COLOR;
                default:
                    // no color for outskirts/other zones
                    break;
            }
            
            if (zoneColor != null) {
                gc.setFill(zoneColor);
                gc.fillRect(zone.getX(), zone.getY(), zone.getWidth(), zone.getHeight());
            }
        }
    }
}

package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.loot.gear.GearType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.Map;

// manages sprite loading
public class SpriteManager {
    private final Map<String, Image> spriteCache = new HashMap<>();
    
    // player sprites by gear type and direction (24 total)
    private Image playerNoGearN, playerNoGearE, playerNoGearS, playerNoGearW;
    private Image playerHazmatN, playerHazmatE, playerHazmatS, playerHazmatW;
    private Image playerRoadsignN, playerRoadsignE, playerRoadsignS, playerRoadsignW;
    private Image playerHeavypotN, playerHeavypotE, playerHeavypotS, playerHeavypotW;
    private Image playerWolfheadN, playerWolfheadE, playerWolfheadS, playerWolfheadW;
    
    // bradley sprites by direction (4 total)
    private Image bradleyN, bradleyE, bradleyS, bradleyW;
    
    // non-directional sprites
    private Image crateNormal;
    private Image crateElite;
    private Image projectile;
    private Image wall;
    
    // load all sprites from resources
    public void loadSprites() {
        // load player sprites
        loadPlayerSprites();
        
        // load Bradley sprites
        loadBradleySprites();
        
        // load other sprites - walls, crates, projectiles
        loadOtherSprites();
    }
    
    private void loadPlayerSprites() {
        // no gear
        playerNoGearN = loadSprite("/com/rust/exfil/takebradley/sprites/player_no_gear_north.png");
        playerNoGearE = loadSprite("/com/rust/exfil/takebradley/sprites/player_no_gear_east.png");
        playerNoGearS = loadSprite("/com/rust/exfil/takebradley/sprites/player_no_gear_south.png");
        playerNoGearW = loadSprite("/com/rust/exfil/takebradley/sprites/player_no_gear_west.png");
        
        // Hazmat
        playerHazmatN = loadSprite("/com/rust/exfil/takebradley/sprites/player_hazmat_north.png");
        playerHazmatE = loadSprite("/com/rust/exfil/takebradley/sprites/player_hazmat_east.png");
        playerHazmatS = loadSprite("/com/rust/exfil/takebradley/sprites/player_hazmat_south.png");
        playerHazmatW = loadSprite("/com/rust/exfil/takebradley/sprites/player_hazmat_west.png");
        
        // Road Sign
        playerRoadsignN = loadSprite("/com/rust/exfil/takebradley/sprites/player_roadsign_north.png");
        playerRoadsignE = loadSprite("/com/rust/exfil/takebradley/sprites/player_roadsign_east.png");
        playerRoadsignS = loadSprite("/com/rust/exfil/takebradley/sprites/player_roadsign_south.png");
        playerRoadsignW = loadSprite("/com/rust/exfil/takebradley/sprites/player_roadsign_west.png");
        
        // Heavy Pot
        playerHeavypotN = loadSprite("/com/rust/exfil/takebradley/sprites/player_heavypot_north.png");
        playerHeavypotE = loadSprite("/com/rust/exfil/takebradley/sprites/player_heavypot_east.png");
        playerHeavypotS = loadSprite("/com/rust/exfil/takebradley/sprites/player_heavypot_south.png");
        playerHeavypotW = loadSprite("/com/rust/exfil/takebradley/sprites/player_heavypot_west.png");
        
        // Wolf Head
        playerWolfheadN = loadSprite("/com/rust/exfil/takebradley/sprites/player_wolfhead_north.png");
        playerWolfheadE = loadSprite("/com/rust/exfil/takebradley/sprites/player_wolfhead_east.png");
        playerWolfheadS = loadSprite("/com/rust/exfil/takebradley/sprites/player_wolfhead_south.png");
        playerWolfheadW = loadSprite("/com/rust/exfil/takebradley/sprites/player_wolfhead_west.png");
    }
    
    private void loadBradleySprites() {
        bradleyN = loadSprite("/com/rust/exfil/takebradley/sprites/tank_north.png");
        bradleyE = loadSprite("/com/rust/exfil/takebradley/sprites/tank_east.png");
        bradleyS = loadSprite("/com/rust/exfil/takebradley/sprites/tank_south.png");
        bradleyW = loadSprite("/com/rust/exfil/takebradley/sprites/tank_west.png");
    }
    
    private void loadOtherSprites() {
        crateNormal = loadSprite("/com/rust/exfil/takebradley/sprites/crate_normal.png");
        crateElite = loadSprite("/com/rust/exfil/takebradley/sprites/crate_elite.png");
        projectile = loadSprite("/com/rust/exfil/takebradley/sprites/projectile.png");
        wall = loadSprite("/com/rust/exfil/takebradley/sprites/wall.png");
    }
    
    /**
     * Load a sprite from resources, return null if not found
     */
    private Image loadSprite(String path) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            spriteCache.put(path, image);
            return image;
        } catch (Exception e) {
            System.err.println("Failed to load sprite: " + path + " - " + e.getMessage());
            return null;
        }
    }
    
    // get player sprite based on direction and gear set
    public Image getPlayerSprite(GearType gearType, Direction direction) {
        if (gearType == null) {
            switch(direction) {
                case UP: return playerNoGearN;
                case RIGHT: return playerNoGearE;
                case DOWN: return playerNoGearS;
                case LEFT: return playerNoGearW;
                default: return playerNoGearS;
            }
        }
        
        switch(gearType) {
            case HAZMAT:
                switch(direction) {
                    case UP: return playerHazmatN;
                    case RIGHT: return playerHazmatE;
                    case DOWN: return playerHazmatS;
                    case LEFT: return playerHazmatW;
                    default: return playerHazmatS;
                }
            case ROADSIGN:
                switch(direction) {
                    case UP: return playerRoadsignN;
                    case RIGHT: return playerRoadsignE;
                    case DOWN: return playerRoadsignS;
                    case LEFT: return playerRoadsignW;
                    default: return playerRoadsignS;
                }
            case HEAVYPOT:
                switch(direction) {
                    case UP: return playerHeavypotN;
                    case RIGHT: return playerHeavypotE;
                    case DOWN: return playerHeavypotS;
                    case LEFT: return playerHeavypotW;
                    default: return playerHeavypotS;
                }
            case WOLFHEAD:
                switch(direction) {
                    case UP: return playerWolfheadN;
                    case RIGHT: return playerWolfheadE;
                    case DOWN: return playerWolfheadS;
                    case LEFT: return playerWolfheadW;
                    default: return playerWolfheadS;
                }
            default:
                return playerNoGearS;
        }
    }
    
    // get bradley apc sprite based on direction
    public Image getBradleySprite(Direction direction) {
        switch(direction) {
            case UP: return bradleyN;
            case RIGHT: return bradleyE;
            case DOWN: return bradleyS;
            case LEFT: return bradleyW;
            default: return bradleyS;
        }
    }
    
    // get crate sprite based on crate type
    public Image getCrateSprite(boolean isElite) {
        return isElite ? crateElite : crateNormal;
    }
    
    public Image getProjectileSprite() {
        return projectile;
    }
    
    public Image getWallSprite() {
        return wall;
    }
    
    // if cant find sprite, draw a placeholder
    public void drawPlaceholder(GraphicsContext gc, double x, double y, double width, double height, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);
    }
}

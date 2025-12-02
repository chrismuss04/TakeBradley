package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.entity.*;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.gear.GearItem;
import com.rust.exfil.takebradley.model.loot.gear.GearType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Renders entities with directional sprites and depth sorting
 */
public class EntityRenderer {
    private final Camera camera;
    
    public EntityRenderer(Camera camera) {
        this.camera = camera;
    }
    
    /**
     * Render all entities sorted by Y coordinate for depth
     */
    public void renderEntities(GraphicsContext gc, List<Entity> entities, SpriteManager sprites) {
        
        // Filter to visible entities and sort by Y
        List<Entity> visibleEntities = entities.stream()
            .filter(e -> e.isAlive())
            .filter(e -> !(e instanceof Projectile)) // Projectiles rendered separately
            .filter(e -> camera.isVisible(e.getX(), e.getY(), 32, 32))
            .sorted(Comparator.comparingDouble(Entity::getY))
            .collect(Collectors.toList());
        
        // Render each entity
        for (Entity entity : visibleEntities) {
            renderEntity(gc, entity, sprites);
        }
    }
    
    /**
     * Render a single entity
     */
    private void renderEntity(GraphicsContext gc, Entity entity, SpriteManager sprites) {
        Image sprite = getEntitySprite(entity, sprites);
        
        if (sprite != null) {
            // Scale sprites to reasonable size
            double targetSize;
            boolean cropPlayerSprite = false;
            
            if (entity instanceof BradleyAPC) {
                targetSize = 64;  // Bradley is 64x64
            } else if (entity instanceof LootCrate || entity instanceof EliteCrate) {
                targetSize = 16;  // Crates are 16x16 (half size)
            } else {
                targetSize = 32;  // Players/NPCs are 32x32
                cropPlayerSprite = true;  // Crop player sprites to remove padding
            }
            
            // Draw sprite centered on entity with scaling
            double x = entity.getX() - targetSize / 2;
            double y = entity.getY() - targetSize / 2;
            
            if (cropPlayerSprite && (entity instanceof Player || entity instanceof NpcPlayer || entity instanceof Scientist)) {
                // Crop player sprites - ONLY crop horizontally to remove side padding
                double sourceWidth = sprite.getWidth();
                double sourceHeight = sprite.getHeight();
                double cropPercentX = 0.55;  // Use center 50% horizontally
                
                // Only crop horizontally, keep full height
                double sourceX = sourceWidth * (1 - cropPercentX) / 2;
                double sourceY = 0;  // No vertical crop
                double sourceCropWidth = sourceWidth * cropPercentX;
                double sourceCropHeight = sourceHeight;  // Full height
                
                // Keep destination size fixed - don't stretch based on aspect ratio
                // This keeps sprites the same size as before, just with sides cropped
                double destWidth = targetSize * 0.7;  // Slightly narrower since we cropped sides
                double destHeight = targetSize;  // Same height as before
                
                // Center the sprite
                double destX = entity.getX() - destWidth / 2;
                double destY = entity.getY() - destHeight / 2;
                
                gc.drawImage(sprite, 
                    sourceX, sourceY, sourceCropWidth, sourceCropHeight,  // Source crop area
                    destX, destY, destWidth, destHeight);  // Destination area (fixed size)
            } else {
                gc.drawImage(sprite, x, y, targetSize, targetSize);
            }
        } else {
            // Draw placeholder if sprite missing
            drawEntityPlaceholder(gc, entity, sprites);
        }
    }
    
    // get the sprite of entity based on type, direction, geartype if applicable
    private Image getEntitySprite(Entity entity, SpriteManager sprites) {
        Direction direction = Direction.DOWN; // Default
        
        // get facing direction for combatants
        if (entity instanceof Combatant) {
            direction = ((Combatant) entity).getFacingDirection();
        }
        
        // select sprite based on entity type, gear type if applicable
        if (entity instanceof Player || entity instanceof NpcPlayer) {
            GearType gearType = getEquippedGearType(entity);
            return sprites.getPlayerSprite(gearType, direction);
        } else if (entity instanceof Scientist) {
            return sprites.getPlayerSprite(GearType.HAZMAT, direction);
        } else if (entity instanceof BradleyAPC) {
            return sprites.getBradleySprite(direction);
        } else if (entity instanceof EliteCrate) {
            return sprites.getCrateSprite(true);
        } else if (entity instanceof LootCrate) {
            return sprites.getCrateSprite(false);
        }
        
        return null;
    }
    
    // get gear type of entity
    private GearType getEquippedGearType(Entity entity) {
        GearItem gear = entity.getInventory().getEquippedGear();
        if (gear == null) {
            return null;
        }
        return gear.getGearType();
    }
    
    private void drawEntityPlaceholder(GraphicsContext gc, Entity entity, SpriteManager sprites) {
        Color color = Color.BLUE; // Default
        double size = 32;
        
        if (entity instanceof Player) {
            color = Color.GREEN;
        } else if (entity instanceof NpcPlayer) {
            color = Color.RED;
        } else if (entity instanceof Scientist) {
            color = Color.YELLOW;
        } else if (entity instanceof BradleyAPC) {
            color = Color.DARKRED;
            size = 64;
        } else if (entity instanceof LootCrate || entity instanceof EliteCrate) {
            color = Color.BROWN;
            size = 24;
        }
        
        sprites.drawPlaceholder(gc, 
            entity.getX() - size/2, 
            entity.getY() - size/2, 
            size, size, color);
    }
}

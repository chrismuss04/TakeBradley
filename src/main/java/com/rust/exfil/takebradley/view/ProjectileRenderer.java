package com.rust.exfil.takebradley.view;

import com.rust.exfil.takebradley.model.entity.Projectile;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;


public class ProjectileRenderer {
    
    public ProjectileRenderer() {
    }
    
    // render all live projectiles
    public void renderProjectiles(GraphicsContext gc, List<Entity> entities, SpriteManager sprites) {
        // filter to live projectiles only
        List<Projectile> projectiles = entities.stream()
            .filter(e -> e instanceof Projectile)
            .map(e -> (Projectile) e)
            .filter(p -> p.isAlive())
            .collect(Collectors.toList());
        
        // render each projectile
        for (Projectile projectile : projectiles) {
            renderProjectile(gc, projectile, sprites);
        }
    }
    
    // render a single projctile
    private void renderProjectile(GraphicsContext gc, Projectile projectile, SpriteManager sprites) {
        Image sprite = sprites.getProjectileSprite();
        
        if (sprite != null) {
            // draw sprite centered on projectile
            double x = projectile.getX() - sprite.getWidth() / 2;
            double y = projectile.getY() - sprite.getHeight() / 2;
            gc.drawImage(sprite, x, y);
        } else {
            // draw placeholder if sprite missing
            drawProjectilePlaceholder(gc, projectile, sprites);
        }
    }
    
    private void drawProjectilePlaceholder(GraphicsContext gc, Projectile projectile, SpriteManager sprites) {
        double size = 6; // small circle for projectile
        Color color = Color.ORANGE;
        
        sprites.drawPlaceholder(gc, 
            projectile.getX() - size/2, 
            projectile.getY() - size/2, 
            size, size, color);
    }
}

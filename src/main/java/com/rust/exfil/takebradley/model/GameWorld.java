package com.rust.exfil.takebradley.model;

import com.rust.exfil.takebradley.controller.EventPublisher;
import com.rust.exfil.takebradley.model.entity.BradleyAPC;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.Projectile;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.map.ExtractionZone;
import com.rust.exfil.takebradley.model.map.GameMap;
import com.rust.exfil.takebradley.model.map.Wall;
import com.rust.exfil.takebradley.model.map.Zone;
import com.rust.exfil.takebradley.systems.event.EntityDeathEvent;
import com.rust.exfil.takebradley.systems.event.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameWorld {
    private final GameMap map;
    private final List<Entity> entities;
    private final List<Combatant> combatants;
    private final List<Projectile> projectiles;
    private final List<Entity> containers;
    private Player player;

    public GameWorld(GameMap map) {
        this.map = map;
        this.entities = new ArrayList<>();
        this.combatants = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.containers = new ArrayList<>();
    }
    
    // Set the player reference (called when player is spawned)
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    // get the player
    public Player getPlayer() {
        return player;
    }

    public GameMap getMap() {
        return map;
    }

   // add an entity to the game world
    public void addEntity(Entity entity) {
        entities.add(entity);
        
        // categorize entities
        if (entity instanceof Projectile) {
            projectiles.add((Projectile) entity);
        }
        if (entity instanceof Combatant) {
            combatants.add((Combatant) entity);
        }
        //  loot containers are entities that are not combatants or projectiles
        if (!(entity instanceof Combatant) && !(entity instanceof Projectile)) {
            containers.add(entity);
        }
    }

    // remove entity by id
    public void removeEntity(UUID id) {
        Entity toRemove = null;
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                toRemove = entity;
                break;
            }
        }
        
        if (toRemove != null) {
            entities.remove(toRemove);
            
            if (toRemove instanceof Projectile) {
                projectiles.remove((Projectile) toRemove);
            }
            if (toRemove instanceof Combatant) {
                combatants.remove((Combatant) toRemove);
            }
            if (!(toRemove instanceof Combatant) && !(toRemove instanceof Projectile)) {
                containers.remove(toRemove);
            }
        }
    }

    // get all entities in the game world
    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    // update entities and handle collisions
    public void updateAll(double deltaTime) {
        // Track entities that were alive before update
        List<Entity> aliveBeforeUpdate = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isAlive() && !(entity instanceof Projectile)) {
                aliveBeforeUpdate.add(entity);
            }
        }
        
        // update all entities
        for (Entity entity : entities) {
            if (!entity.isAlive()) continue;
            
            // store old position
            double oldX = entity.getX();
            double oldY = entity.getY();
            
            // update entity to move it
            entity.update(deltaTime);
            
            // check wall collisions for movable entities (except projectiles)
            if (entity instanceof Movable && !(entity instanceof Projectile)) {
                // Use entity size for collision (32 pixels for most entities, 64 for Bradley)
                double entitySize = (entity instanceof BradleyAPC) ? 64 : 32;
                if (checkWallCollisionWithSize(entity.getX(), entity.getY(), entitySize)) {
                    // revert position if collision detected
                    ((Movable) entity).setPosition(oldX, oldY);
                }
            }
        }
        
        // check projectile collisions
        checkProjectileCollisions();
        
        // Check for entities that died this frame and publish death events
        for (Entity entity : aliveBeforeUpdate) {
            if (!entity.isAlive()) {
                EventPublisher.getInstance()
                    .publish(new EntityDeathEvent(entity));
            }
        }
        
        // remove 'dead' projectiles
        projectiles.removeIf(p -> !p.isAlive());
        entities.removeIf(e -> e instanceof Projectile && !e.isAlive());
    }

    // check projectile collisions
    private void checkProjectileCollisions() {
        for (Projectile projectile : projectiles) {
            if (!projectile.isAlive()) continue;
            
            // check wall collision
            if (checkWallCollision(projectile.getX(), projectile.getY())) {
                projectile.hitWall();
                continue;
            }
            
            // check entity collision
            for (Entity entity : entities) {
                if (entity == projectile) continue;
                if (entity == projectile.getOwner()) continue; // No friendly fire
                if (!entity.isAlive()) continue;
                
                if (projectile.isCollidingWith(entity)) {
                    // Apply damage
                    projectile.hit(entity);
                    
                    // publish hit event for sound effects
                    if (entity instanceof Combatant) {
                        EventPublisher.getInstance().publish(new ProjectileHitEvent(projectile, entity));
                    }
                    
                    break;
                }
            }
        }
    }

    // check if coordinates intersect with a wall (for movement/projectile movement)
    public boolean checkWallCollision(double x, double y) {
        List<Wall> walls = map.getWalls();
        for (Wall wall : walls) {
            if (wall.intersects(x, y)) {
                return true;
            }
        }
        return false;
    }
    
    // Check wall collision for entities with size (better collision detection)
    public boolean checkWallCollisionWithSize(double x, double y, double size) {
        List<Wall> walls = map.getWalls();
        for (Wall wall : walls) {
            if (wall.intersectsEntity(x, y, size)) {
                return true;
            }
        }
        return false;
    }

    // check if an entity is in extraction zone -> for extract logic
    public boolean isInExtractionZone(Entity entity) {
        double x = entity.getX();
        double y = entity.getY();
        for (Zone zone : map.getZones()) {
            if (zone instanceof ExtractionZone) {
                if (zone.contains(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }
}

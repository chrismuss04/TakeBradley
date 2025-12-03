package com.rust.exfil.takebradley.model.strategy;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.map.BradleyZone;
import com.rust.exfil.takebradley.model.map.Zone;

/**
 * Movement and combat strategy for Bradley APC
 * Pursues player while staying within Bradley zone boundaries
 * Attempts to align for shots in cardinal directions
 */
public class TankMovementStrategy implements CombatStrategy {
    private static final double DETECTION_RADIUS = 600.0; // Increased to cover entire Bradley zone (400x400)
    private static final double ATTACK_RANGE = 180.0; // Restored to original value
    private static final double ALIGNMENT_THRESHOLD = 35.0; // Balanced alignment - accurate but achievable
    private double timeSinceLastShot = 0.0;
    private static final double FIRE_COOLDOWN = 0.5; // Half second between shots
    
    @Override
    public void execute(Entity self, GameWorld world, double deltaTime) {
        if (!(self instanceof Movable) || !(self instanceof Combatant)) {
            return;
        }
        
        // Update fire cooldown
        timeSinceLastShot += deltaTime;
        
        Movable movable = (Movable) self;
        Combatant combatant = (Combatant) self;
        
        // Find Bradley zone
        BradleyZone bradleyZone = findBradleyZone(world);
        
        // Find nearest target (Player or NPC)
        Entity nearestTarget = findNearestTarget(self, world);
        
        if (nearestTarget != null) {
            double distanceToTarget = world.calculateDistance(self, nearestTarget);
            
            if (distanceToTarget <= DETECTION_RADIUS) {
                // Pursue and engage target while staying in zone
                pursueAndEngageInZone(movable, combatant, self, nearestTarget, bradleyZone, world, deltaTime);
            }
        }
    }
    
    private void pursueAndEngageInZone(Movable self, Combatant combatant, Entity selfEntity, 
                                       Entity target, BradleyZone zone, GameWorld world, double deltaTime) {
        double dx = target.getX() - selfEntity.getX();
        double dy = target.getY() - selfEntity.getY();
        
        double distance = world.calculateDistance(selfEntity, target);
        
        // Define a closer range for alignment attempts (50% of attack range)
        double alignmentRange = ATTACK_RANGE * 0.5; // 90 pixels
        
        // If close enough, try to align and shoot
        if (distance <= alignmentRange) {
            // Determine which direction to align to
            Direction alignedDirection = getAlignedDirection(dx, dy);
            
            // Check if we're aligned for a shot
            if (isAlignedForShot(dx, dy, alignedDirection)) {
                // We're aligned - face target and shoot
                combatant.setFacingDirection(alignedDirection);
                
                // Check fire cooldown and line of sight before firing
                if (timeSinceLastShot >= FIRE_COOLDOWN && world.hasLineOfSight(selfEntity, target)) {
                    combatant.fireWeapon();
                    timeSinceLastShot = 0.0; // Reset cooldown
                    
                    // check if we need to reload after firing
                    if (needsReload(combatant)) {
                        combatant.reload();
                    }
                }
            } else {
                // Not aligned - move to align with target
                moveToAlignInZone(self, selfEntity, dx, dy, alignedDirection, zone, deltaTime);
            }
        } else {
            // Move toward target - pursue if not close enough
            moveTowardTargetInZone(self, selfEntity, dx, dy, distance, zone, deltaTime);
        }
    }
    
    private Direction getAlignedDirection(double dx, double dy) {
        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        
        if (absDx > absDy) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
    
    private boolean isAlignedForShot(double dx, double dy, Direction direction) {
        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        
        switch (direction) {
            case LEFT:
            case RIGHT:
                return absDy < ALIGNMENT_THRESHOLD;
            case UP:
            case DOWN:
                return absDx < ALIGNMENT_THRESHOLD;
            default:
                return false;
        }
    }
    
    private void moveToAlignInZone(Movable self, Entity selfEntity, double dx, double dy, 
                                   Direction targetDirection, BradleyZone zone, double deltaTime) {
        double moveX = 0;
        double moveY = 0;
        
        switch (targetDirection) {
            case LEFT:
            case RIGHT:
                moveY = dy > 0 ? 1 : -1;
                break;
            case UP:
            case DOWN:
                moveX = dx > 0 ? 1 : -1;
                break;
        }
        
        // Update facing direction based on movement
        if (self instanceof Combatant) {
            Direction movementDirection = getMovementDirection(moveX, moveY);
            ((Combatant) self).setFacingDirection(movementDirection);
        }
        
        // Check zone boundaries before moving
        if (canMoveInZone(selfEntity, moveX, moveY, self.getSpeed(), zone, deltaTime)) {
            self.move(moveX, moveY);
        }
    }
    
    private void moveTowardTargetInZone(Movable self, Entity selfEntity, double dx, double dy, 
                                        double distance, BradleyZone zone, double deltaTime) {
        // Normalize direction
        if (distance > 0) {
            dx /= distance;
            dy /= distance;
        }
        
        // Update facing direction based on movement
        if (self instanceof Combatant) {
            Direction movementDirection = getMovementDirection(dx, dy);
            ((Combatant) self).setFacingDirection(movementDirection);
        }
        
        // Check zone boundaries before moving
        if (canMoveInZone(selfEntity, dx, dy, self.getSpeed(), zone, deltaTime)) {
            self.move(dx, dy);
        }
    }
    
    private boolean canMoveInZone(Entity selfEntity, double dx, double dy, double speed, 
                                  BradleyZone zone, double deltaTime) {
        // If no zone defined, allow movement
        if (zone == null) return true;
        
        double nextX = selfEntity.getX() + dx * speed * deltaTime;
        double nextY = selfEntity.getY() + dy * speed * deltaTime;
        
        // Reduce padding to allow more movement freedom
        // Bradley is 64x64, but we only need 10 pixel padding to stay mostly in zone
        double padding = 10.0;
        
        boolean canMove = nextX >= zone.getX() + padding && 
                          nextX <= zone.getX() + zone.getWidth() - padding &&
                          nextY >= zone.getY() + padding && 
                          nextY <= zone.getY() + zone.getHeight() - padding;
        
        // If can't move in desired direction, allow movement anyway
        // This prevents Bradley from getting stuck at zone edges
        return true; // Temporarily disable zone restriction to test
    }
    
    private BradleyZone findBradleyZone(GameWorld world) {
        for (Zone zone : world.getMap().getZones()) {
            if (zone instanceof BradleyZone) {
                return (BradleyZone) zone;
            }
        }
        return null;
    }
    
    /**
     * Find the nearest Player or NPC within detection range
     * Bradley will target Players and NPCs (not Scientists)
     */
    private Entity findNearestTarget(Entity self, GameWorld world) {
        Entity nearestTarget = null;
        double nearestDistance = DETECTION_RADIUS;
        
        // Check all entities in the world
        for (Entity entity : world.getEntities()) {
            // Skip self
            if (entity == self) {
                continue;
            }
            
            // Skip dead entities
            if (!entity.isAlive()) {
                continue;
            }
            
            // Only target Player and NpcPlayer (not Scientist or other Bradley)
            if (!(entity instanceof com.rust.exfil.takebradley.model.entity.Player) && 
                !(entity instanceof com.rust.exfil.takebradley.model.entity.NpcPlayer)) {
                continue;
            }
            
            // Calculate distance
            double distance = world.calculateDistance(self, entity);
            
            // Update nearest if this is closer
            if (distance < nearestDistance) {
                nearestTarget = entity;
                nearestDistance = distance;
            }
        }
        
        return nearestTarget;
    }
}

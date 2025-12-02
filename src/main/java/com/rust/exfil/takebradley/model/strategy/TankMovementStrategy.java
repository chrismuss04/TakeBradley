package com.rust.exfil.takebradley.model.strategy;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
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
    private static final double DETECTION_RADIUS = 300.0;
    private static final double ATTACK_RANGE = 200.0;
    private static final double ALIGNMENT_THRESHOLD = 30.0; // Bradley is less precise
    
    @Override
    public void execute(Entity self, GameWorld world, double deltaTime) {
        if (!(self instanceof Movable) || !(self instanceof Combatant)) {
            return;
        }
        
        Movable movable = (Movable) self;
        Combatant combatant = (Combatant) self;
        Player player = world.getPlayer();
        
        // Find Bradley zone
        BradleyZone bradleyZone = findBradleyZone(world);
        
        // Check if player is detected
        if (player != null && player.isAlive()) {
            double distanceToPlayer = world.calculateDistance(self, player);
            
            if (distanceToPlayer <= DETECTION_RADIUS) {
                // Pursue and engage player while staying in zone
                pursueAndEngageInZone(movable, combatant, self, player, bradleyZone, world, deltaTime);
            }
        }
    }
    
    private void pursueAndEngageInZone(Movable self, Combatant combatant, Entity selfEntity, 
                                       Entity target, BradleyZone zone, GameWorld world, double deltaTime) {
        double dx = target.getX() - selfEntity.getX();
        double dy = target.getY() - selfEntity.getY();
        
        double distance = world.calculateDistance(selfEntity, target);
        
        // check if in attack range
        if (distance <= ATTACK_RANGE) {
            // try to align for a shot
            Direction alignedDirection = getAlignedDirection(dx, dy);
            
            if (isAlignedForShot(dx, dy, alignedDirection)) {
                // update facing direction
                combatant.setFacingDirection(alignedDirection);
                
                // check line of sight before firing
                if (world.hasLineOfSight(selfEntity, target)) {
                    combatant.fireWeapon();
                    
                    // check if we need to reload after firing
                    if (needsReload(combatant)) {
                        combatant.reload();
                    }
                }
            } else {
                // move to align with target (within zone)
                moveToAlignInZone(self, selfEntity, dx, dy, alignedDirection, zone, deltaTime);
            }
        } else {
            // move toward target (within zone)
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
        
        // Check zone boundaries before moving
        if (canMoveInZone(selfEntity, moveX, moveY, self.getSpeed(), zone, deltaTime)) {
            self.move(moveX * deltaTime, moveY * deltaTime);
        }
    }
    
    private void moveTowardTargetInZone(Movable self, Entity selfEntity, double dx, double dy, 
                                        double distance, BradleyZone zone, double deltaTime) {
        if (distance > 0) {
            dx /= distance;
            dy /= distance;
        }
        
        // Check zone boundaries before moving
        if (canMoveInZone(selfEntity, dx, dy, self.getSpeed(), zone, deltaTime)) {
            self.move(dx * deltaTime, dy * deltaTime);
        }
    }
    
    private boolean canMoveInZone(Entity selfEntity, double dx, double dy, double speed, 
                                  BradleyZone zone, double deltaTime) {
        if (zone == null) return true;
        
        double nextX = selfEntity.getX() + dx * speed * deltaTime;
        double nextY = selfEntity.getY() + dy * speed * deltaTime;
        
        return zone.contains(nextX, nextY);
    }
    
    private BradleyZone findBradleyZone(GameWorld world) {
        for (Zone zone : world.getMap().getZones()) {
            if (zone instanceof BradleyZone) {
                return (BradleyZone) zone;
            }
        }
        return null;
    }
}

package com.rust.exfil.takebradley.model.strategy;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.NpcPlayer;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.map.BradleyZone;
import com.rust.exfil.takebradley.model.map.Zone;

public class TankMovementStrategy implements CombatStrategy {
    private static final double DETECTION_RADIUS = 600.0; 
    private static final double ATTACK_RANGE = 180.0; 
    private static final double ALIGNMENT_THRESHOLD = 35.0;
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
        
        // Check if Bradley is outside its zone and return if so
        if (bradleyZone != null && !isInZone(self, bradleyZone)) {
            returnToZone(movable, combatant, self, bradleyZone, deltaTime);
            return;
        }
        
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
        
        double alignmentRange = ATTACK_RANGE * 0.5;
        
        // if close enough, try to align and shoot
        if (distance <= alignmentRange) {
            // determine which direction to align to
            Direction alignedDirection = getAlignedDirection(dx, dy);
            
            if (isAlignedForShot(dx, dy, alignedDirection)) {
                combatant.setFacingDirection(alignedDirection);
                
                // check fire cooldown and line of sight before firing
                if (timeSinceLastShot >= FIRE_COOLDOWN && world.hasLineOfSight(selfEntity, target)) {
                    combatant.fireWeapon();
                    timeSinceLastShot = 0.0;
                    
                    // check if we need to reload after firing
                    if (needsReload(combatant)) {
                        combatant.reload();
                    }
                }
            } else {
                // not aligned, move to align with target
                moveToAlignInZone(self, selfEntity, dx, dy, alignedDirection, zone, deltaTime);
            }
        } else {
            // move toward target, pursue if not close enough
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
        if (zone == null) return true;
        
        double nextX = selfEntity.getX() + dx * speed * deltaTime;
        double nextY = selfEntity.getY() + dy * speed * deltaTime;
        
        // Bradley is 64x64, so 32 pixel padding keeps it fully in zone
        double padding = 32.0;
        
        boolean canMove = nextX >= zone.getX() + padding && 
                          nextX <= zone.getX() + zone.getWidth() - padding &&
                          nextY >= zone.getY() + padding && 
                          nextY <= zone.getY() + zone.getHeight() - padding;
        
        return canMove;
    }
    
    private BradleyZone findBradleyZone(GameWorld world) {
        for (Zone zone : world.getMap().getZones()) {
            if (zone instanceof BradleyZone) {
                return (BradleyZone) zone;
            }
        }
        return null;
    }
    
    private Entity findNearestTarget(Entity self, GameWorld world) {
        Entity nearestTarget = null;
        double nearestDistance = DETECTION_RADIUS;
        
        // check all entities in the world
        for (Entity entity : world.getEntities()) {
            // Skip self
            if (entity == self) {
                continue;
            }
            
            // skip dead entities
            if (!entity.isAlive()) {
                continue;
            }
            
            // only target Player and NpcPlayer
            if (!(entity instanceof Player) && 
                !(entity instanceof NpcPlayer)) {
                continue;
            }
            
            // calculate distance
            double distance = world.calculateDistance(self, entity);
            
            // update nearest if this is closer
            if (distance < nearestDistance) {
                nearestTarget = entity;
                nearestDistance = distance;
            }
        }
        
        return nearestTarget;
    }
    
    private boolean isInZone(Entity self, BradleyZone zone) {
        if (zone == null) return true;
        
        double padding = 32.0; // Same padding as canMoveInZone to prevent flickering
        double x = self.getX();
        double y = self.getY();
        
        return x >= zone.getX() + padding && 
               x <= zone.getX() + zone.getWidth() - padding &&
               y >= zone.getY() + padding && 
               y <= zone.getY() + zone.getHeight() - padding;
    }
    
    private void returnToZone(Movable self, Combatant combatant, Entity selfEntity, 
                              BradleyZone zone, double deltaTime) {
        // calculate zone center
        double zoneCenterX = zone.getX() + zone.getWidth() / 2;
        double zoneCenterY = zone.getY() + zone.getHeight() / 2;
        
        // calculate direction to zone center
        double dx = zoneCenterX - selfEntity.getX();
        double dy = zoneCenterY - selfEntity.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            dx /= distance;
            dy /= distance;
        }
        
        // update facing direction
        Direction movementDirection = getMovementDirection(dx, dy);
        combatant.setFacingDirection(movementDirection);
        
        self.move(dx, dy);
    }
}

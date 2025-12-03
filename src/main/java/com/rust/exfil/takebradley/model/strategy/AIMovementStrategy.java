package com.rust.exfil.takebradley.model.strategy;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;

import java.util.Random;

// movement/combat strategy for npc players
public class AIMovementStrategy implements CombatStrategy {
    private static final double DETECTION_RADIUS = 130.0; // Detection range
    private static final double ATTACK_RANGE = 90.0; // Attack range
    private static final double DIRECTION_CHANGE_INTERVAL = 2.0; // seconds
    private static final double ALIGNMENT_THRESHOLD = 5.0; // Balanced alignment - accurate but achievable
    private static final double FIRE_COOLDOWN = 0.3; // Cooldown between shots
    
    private final Random random;
    private double currentDirectionX;
    private double currentDirectionY;
    private double timeSinceDirectionChange;
    private double timeSinceLastShot = 0.0;
    
    public AIMovementStrategy() {
        this.random = new Random();
        this.timeSinceDirectionChange = 0;
        chooseRandomDirection();
    }
    
    public AIMovementStrategy(long seed) {
        this.random = new Random(seed);
        this.timeSinceDirectionChange = 0;
        chooseRandomDirection();
    }
    
    @Override
    public void execute(Entity self, GameWorld world, double deltaTime) {
        if (!(self instanceof Movable) || !(self instanceof Combatant)) {
            return;
        }
        
        // Update fire cooldown
        timeSinceLastShot += deltaTime;
        
        Movable movable = (Movable) self;
        Combatant combatant = (Combatant) self;
        
        // Find nearest combatant (including player and other NPCs)
        Entity nearestTarget = findNearestCombatant(self, world);
        
        if (nearestTarget != null) {
            double distanceToTarget = world.calculateDistance(self, nearestTarget);
            
            if (distanceToTarget <= DETECTION_RADIUS) {
                // pursue and engage nearest target
                pursueAndEngageTarget(movable, combatant, self, nearestTarget, world, deltaTime);
                return;
            }
        }
        
        // random roaming behavior
        roam(movable, deltaTime);
    }
    
    private void pursueAndEngageTarget(Movable self, Combatant combatant, Entity selfEntity, 
                                       Entity target, GameWorld world, double deltaTime) {
        // calculate distance to target
        double dx = target.getX() - selfEntity.getX();
        double dy = target.getY() - selfEntity.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Define a closer range for alignment attempts (70% of attack range)
        double alignmentRange = ATTACK_RANGE * 0.7;
        
        // check if in close range for alignment
        if (distance <= alignmentRange) {
            // check if aligned for shot
            Direction alignedDirection = getAlignedDirection(dx, dy);   
            if (isAlignedForShot(dx, dy, alignedDirection)) {
                // update facing direction
                combatant.setFacingDirection(alignedDirection);
                
                // Check fire cooldown and line of sight before firing
                if (timeSinceLastShot >= FIRE_COOLDOWN && world.hasLineOfSight(selfEntity, target)) {
                    combatant.fireWeapon();
                    timeSinceLastShot = 0.0; // Reset cooldown
                    
                    // Check if we need to reload after firing
                    if (needsReload(combatant)) {
                        combatant.reload();
                    }
                }
            } else {
                // move to align with target
                moveToAlign(self, dx, dy, distance, alignedDirection, deltaTime);
            }
        } else {
            // move toward target - pursue if too far, normalize direction
            if (distance > 0) {
                dx /= distance;
                dy /= distance;
            }
            
            // Update facing direction based on movement
            if (self instanceof Combatant) {
                Direction movementDirection = getMovementDirection(dx, dy);
                ((Combatant) self).setFacingDirection(movementDirection);
            }
            
            self.move(dx, dy);
        }
    }
    
    private Direction getAlignedDirection(double dx, double dy) {
        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        
        // Determine which cardinal direction is closest
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
    
    private void moveToAlign(Movable self, double dx, double dy, double distance, 
                            Direction targetDirection, double deltaTime) {
        // move perpendicular to the misalignment to get into line
        double moveX = 0;
        double moveY = 0;
        
        switch (targetDirection) {
            case LEFT:
            case RIGHT:
                // need to align vertically, move up or down
                moveY = dy > 0 ? 1 : -1;
                break;
            case UP:
            case DOWN:
                // need to align horizontally, move left or right
                moveX = dx > 0 ? 1 : -1;
                break;
        }
        
        // Update facing direction based on movement
        if (self instanceof Combatant) {
            Direction movementDirection = getMovementDirection(moveX, moveY);
            ((Combatant) self).setFacingDirection(movementDirection);
        }
        
        self.move(moveX, moveY);
    }
    
    private void roam(Movable self, double deltaTime) {
        timeSinceDirectionChange += deltaTime;
        
        // change direction periodically
        if (timeSinceDirectionChange >= DIRECTION_CHANGE_INTERVAL) {
            chooseRandomDirection();
            timeSinceDirectionChange = 0;
        }
        
        // Update facing direction based on roaming movement
        if (self instanceof Combatant) {
            Direction movementDirection = getMovementDirection(currentDirectionX, currentDirectionY);
            ((Combatant) self).setFacingDirection(movementDirection);
        }
        
        self.move(currentDirectionX, currentDirectionY);
    }
    
    private void chooseRandomDirection() {
        // generate random angle
        double angle = random.nextDouble() * 2 * Math.PI;
        currentDirectionX = Math.cos(angle);
        currentDirectionY = Math.sin(angle);
    }
    
    // find the nearest combatant for NPCPlayer to target
    // NPCs only target Player and other NPCs (not Scientists or Bradley)
    private Entity findNearestCombatant(Entity self, GameWorld world) {
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
            
            // Only target Player and NpcPlayer (not Scientist or Bradley)
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

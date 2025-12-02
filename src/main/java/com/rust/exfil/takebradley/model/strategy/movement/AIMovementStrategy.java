package com.rust.exfil.takebradley.model.strategy.movement;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;

import java.util.Random;

// movement/combat strategy for npc players
public class AIMovementStrategy implements CombatStrategy {
    private static final double DETECTION_RADIUS = 200.0;
    private static final double ATTACK_RANGE = 150.0;
    private static final double DIRECTION_CHANGE_INTERVAL = 2.0; // seconds
    private static final double ALIGNMENT_THRESHOLD = 20.0; // pixels - how close to aligned before firing
    
    private final Random random;
    private double currentDirectionX;
    private double currentDirectionY;
    private double timeSinceDirectionChange;
    
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
        
        Movable movable = (Movable) self;
        Combatant combatant = (Combatant) self;
        Player player = world.getPlayer();
        
        // check if player is detected
        if (player != null && player.isAlive()) {
            double distanceToPlayer = world.calculateDistance(self, player);
            
            if (distanceToPlayer <= DETECTION_RADIUS) {
                // pursue and engage player
                pursueAndEngageTarget(movable, combatant, self, player, world, deltaTime);
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
        
        // check if in attack range
        if (distance <= ATTACK_RANGE) {
            // check if aligned for shot
            Direction alignedDirection = getAlignedDirection(dx, dy);   
            if (isAlignedForShot(dx, dy, alignedDirection)) {
                // update facing direction
                combatant.setFacingDirection(alignedDirection);
                
                // Check line of sight before firing
                if (world.hasLineOfSight(selfEntity, target)) {
                    combatant.fireWeapon();
                    
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
            // move toward target
            if (distance > 0) {
                dx /= distance;
                dy /= distance;
            }
            self.move(dx * deltaTime, dy * deltaTime);
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
        
        self.move(moveX * deltaTime, moveY * deltaTime);
    }
    
    private void roam(Movable self, double deltaTime) {
        timeSinceDirectionChange += deltaTime;
        
        // change direction periodically
        if (timeSinceDirectionChange >= DIRECTION_CHANGE_INTERVAL) {
            chooseRandomDirection();
            timeSinceDirectionChange = 0;
        }
        
        // move in current direction
        self.move(currentDirectionX * deltaTime, currentDirectionY * deltaTime);
    }
    
    private void chooseRandomDirection() {
        // generate random angle
        double angle = random.nextDouble() * 2 * Math.PI;
        currentDirectionX = Math.cos(angle);
        currentDirectionY = Math.sin(angle);
    }
}

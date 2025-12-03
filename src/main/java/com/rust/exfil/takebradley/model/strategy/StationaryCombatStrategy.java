package com.rust.exfil.takebradley.model.strategy;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

// combat strategy for scientiests - no movement
public class StationaryCombatStrategy implements CombatStrategy {
    private static final double ATTACK_RANGE = 100.0;
    private static final double ALIGNMENT_THRESHOLD = 25.0; // how close to aligned before firing
    private static final double FIRE_COOLDOWN = 0.4; // cooldown between shots
    private double timeSinceLastShot = 0.0;
    
    @Override
    public void execute(Entity self, GameWorld world, double deltaTime) {
        if (!(self instanceof Combatant)) {
            return;
        }
        
        // Update fire cooldown
        timeSinceLastShot += deltaTime;
        
        Combatant combatant = (Combatant) self;
        Player player = world.getPlayer();
        
        if (player != null && player.isAlive()) {
            double distance = world.calculateDistance(self, player);
            
            if (distance <= ATTACK_RANGE) {
                engageTarget(combatant, self, player, world);
            }
        }
    }
    
    private void engageTarget(Combatant combatant, Entity self, Entity target, GameWorld world) {
        double dx = target.getX() - self.getX();
        double dy = target.getY() - self.getY();
        
        // Face the target and shoot (bloom handles inaccuracy)
        Direction facingDirection = getAlignedDirection(dx, dy);
        combatant.setFacingDirection(facingDirection);
        
        // Check fire cooldown and line of sight before firing
        if (timeSinceLastShot >= FIRE_COOLDOWN && world.hasLineOfSight(self, target)) {
            combatant.fireWeapon();
            timeSinceLastShot = 0.0; // Reset cooldown
            
            // check if we need to reload after firing
            if (needsReload(combatant)) {
                combatant.reload();
            }
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
}

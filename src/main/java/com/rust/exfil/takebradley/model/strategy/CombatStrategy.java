package com.rust.exfil.takebradley.model.strategy;

import com.rust.exfil.takebradley.model.Direction;
import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

// for ai combat and movement
public interface CombatStrategy {
    
    void execute(Entity self, GameWorld world, double deltaTime);
    
    // check if combatant needs to reload
    default boolean needsReload(Combatant combatant) {
        if (!(combatant instanceof Entity)) return false;
        
        Entity entity = (Entity) combatant;
        int slotIndex = combatant.getSelectedSlotIndex();
        if (entity.getInventory() == null) return false;
        
        LootItem item = entity.getInventory().getItem(slotIndex);
        if (item instanceof WeaponItem) {
            WeaponItem weapon = (WeaponItem) item;
            return weapon.getCurrentAmmo() == 0;
        }
        return false;
    }
    
    // get movement direction based on dx/dy vector
    default Direction getMovementDirection(double dx, double dy) {
        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        
        // Determine primary movement direction
        if (absDx > absDy) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
}

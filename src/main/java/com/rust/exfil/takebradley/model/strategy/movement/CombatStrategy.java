package com.rust.exfil.takebradley.model.strategy.movement;

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
}

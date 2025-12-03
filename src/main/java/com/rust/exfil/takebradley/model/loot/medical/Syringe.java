package com.rust.exfil.takebradley.model.loot.medical;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class Syringe extends MedicalItem{
    Syringe() {
        this.name = "Syringe";
        this.description = "A medical syringe used to heal moderate damage";
        this.medType = MedType.SYRINGE;
        this.quantity = 3;
    }
    @Override
    public void use(Entity user) {
        if(user instanceof Combatant){
            Combatant combatant = (Combatant)user;
            combatant.heal(40);
            
            // Decrement quantity
            quantity--;
            
            // Remove from inventory if quantity reaches 0
            if (quantity <= 0) {
                int i = combatant.getSelectedSlotIndex();
                user.getInventory().removeItem(i);
            }
        }

    }
}

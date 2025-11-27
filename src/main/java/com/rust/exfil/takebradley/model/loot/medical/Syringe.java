package com.rust.exfil.takebradley.model.loot.medical;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class Syringe extends MedicalItem{
    Syringe() {
        this.name = "Syringe";
        this.description = "A medical syringe used to heal moderate damage";
    }
    @Override
    public void use(Entity user) {
        if(user instanceof Combatant){
            Combatant combatant = (Combatant)user;
            combatant.heal(40);
            int i =  combatant.getSelectedSlotIndex();
            user.getInventory().removeItem(i);
        }

    }
}

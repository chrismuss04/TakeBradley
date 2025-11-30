package com.rust.exfil.takebradley.model.loot.medical;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class Bandage extends MedicalItem{
    Bandage() {
        this.name = "Bandage";
        this.description = "A simple bandage used to heal minor damage";
        this.quantity = 3;
    }
    @Override
    public void use(Entity user) {
       if (user instanceof Combatant) {
           Combatant combatant = (Combatant) user;
           combatant.heal(15);
           int i =  combatant.getSelectedSlotIndex();
           user.getInventory().removeItem(i);
       }
    }
}

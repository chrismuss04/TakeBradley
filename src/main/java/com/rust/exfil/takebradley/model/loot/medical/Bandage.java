package com.rust.exfil.takebradley.model.loot.medical;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;

public class Bandage extends MedicalItem{
    Bandage() {
        this.name = "Bandage";
        this.description = "A simple bandage used to heal minor damage";
        this.medType = MedType.BANDAGE;
        this.quantity = 3;
    }
    @Override
    public void use(Entity user) {
       if (user instanceof Combatant) {
           Combatant combatant = (Combatant) user;
           combatant.heal(15);
           
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

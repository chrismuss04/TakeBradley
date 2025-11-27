package com.rust.exfil.takebradley.model.buff;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;

public class DamageResist implements Buff {
    String name;
    String description;
    double resistancePercent;

    DamageResist(double resistancePercent) {
        this.resistancePercent = resistancePercent;
        this.name = "Damage Resistance";
        this.description = "+" + (int)(resistancePercent * 100) + "% damage resistance";
    }
    @Override
    public void apply(Combatant combatant) {
        double currentResist = combatant.getDamageResistance();
        combatant.setDamageResistance(currentResist + resistancePercent);
    }

    @Override
    public void remove(Combatant combatant) {
        double currentResist = combatant.getDamageResistance();
        combatant.setDamageResistance(currentResist - resistancePercent);

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

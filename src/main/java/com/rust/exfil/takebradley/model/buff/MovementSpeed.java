package com.rust.exfil.takebradley.model.buff;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;

public class MovementSpeed implements Buff{
    String name;
    String description;
    double speedModifyPercent;

    MovementSpeed(double speedModifyPercent) {
        this.speedModifyPercent = speedModifyPercent;
        this.name = "Movement Speed Decrease";
        this.description = "-" + (int)(speedModifyPercent * 100) + "% movement speed";
    }

    @Override
    public void apply(Combatant combatant) {
        if (combatant instanceof Movable) {
            Movable movableCombatant = (Movable) combatant;
            double currentSpeed = movableCombatant.getSpeed();
            movableCombatant.setSpeed(currentSpeed * (1 + speedModifyPercent));
        }
    }

    @Override
    public void remove(Combatant combatant) {
        if (combatant instanceof Movable) {
            Movable movableCombatant = (Movable) combatant;
            double currentSpeed = movableCombatant.getSpeed();
            movableCombatant.setSpeed(currentSpeed / (1 + speedModifyPercent));
        }
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

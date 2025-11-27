package com.rust.exfil.takebradley.model.buff;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;

public interface Buff {
    void apply(Combatant combatant);
    void remove(Combatant combatant);
    String getName();
    String getDescription();
}

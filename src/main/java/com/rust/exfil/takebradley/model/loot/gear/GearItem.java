package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.buff.Buff;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import java.util.ArrayList;
import java.util.List;

public abstract class GearItem implements LootItem {
    String name;
    String description;
    GearType gearType;
    List<Buff> buffs = new ArrayList<>();

    void addBuff(Buff buff) {
        buffs.add(buff);
    }
    
    public GearType getGearType() {
        return gearType;
    }

    public void applyBuffs(Entity user) {
        if (user instanceof Combatant) {
            Combatant combatant = (Combatant) user;
            for (Buff buff : buffs) {
                buff.apply(combatant);
            }
        }
    }

    public void removeBuffs(Entity user) {
        if (user instanceof Combatant) {
            Combatant combatant = (Combatant) user;
            for (Buff buff : buffs) {
                buff.remove(combatant);
            }
        }
    }

    public List<Buff> getBuffs() {
        return new ArrayList<>(buffs);
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

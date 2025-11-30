package com.rust.exfil.takebradley.model.entity.interfaces;

import com.rust.exfil.takebradley.model.Direction;

public interface Combatant {
    int getHealth();
    int getMaxHealth();
    void takeDamage(int damage);
    void heal(int amount);
    void die();

    void fireWeapon();
    void reload();

    int getSelectedSlotIndex();

    double getDamageResistance();
    void setDamageResistance(double resistance);
    
    Direction getFacingDirection();
    void setFacingDirection(Direction direction);
}

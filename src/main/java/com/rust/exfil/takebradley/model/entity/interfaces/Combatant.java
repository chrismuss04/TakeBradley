package com.rust.exfil.takebradley.model.entity.interfaces;

public interface Combatant {
    int getHealth();
    int getMaxHealth();
    void takeDamage(int damage);
    void heal(int amount);
    void die();

    void fireWeapon();
    void reload();

    int getSelectedSlotIndex();
}

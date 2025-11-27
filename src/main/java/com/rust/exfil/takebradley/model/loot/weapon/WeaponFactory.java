package com.rust.exfil.takebradley.model.loot.weapon;

public class WeaponFactory {
    public static WeaponItem create(WeaponType type) {
        switch (type) {
            case ROCKET_LAUNCHER:
                return new RocketLauncher();
            case AK:
                return new AK();
            case MP5:
                return new MP5();
            case P2:
                return new P2();
            default:
                throw new IllegalArgumentException("Unknown weapon type: " + type);
        }
    }
}

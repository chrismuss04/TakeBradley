package com.rust.exfil.takebradley.model.loot.weapon;

public class WeaponFactory {
    public AK createAK() {
        return new AK();
    }
    public MP5 createMP5() {
        return new MP5();
    }
    public P2 createP2() {
        return new P2();
    }
    public RocketLauncher createRocketLauncher() {
        return new RocketLauncher();
    }
}

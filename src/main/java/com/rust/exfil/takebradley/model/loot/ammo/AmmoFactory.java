package com.rust.exfil.takebradley.model.loot.ammo;

public class AmmoFactory {
    public PistolAmmo createPistolAmmo() {
        return new PistolAmmo();
    }
    public RifleAmmo createRifleAmmo() {
        return new RifleAmmo();
    }
    public RocketAmmo createRocketAmmo() {
        return new RocketAmmo();
    }
}

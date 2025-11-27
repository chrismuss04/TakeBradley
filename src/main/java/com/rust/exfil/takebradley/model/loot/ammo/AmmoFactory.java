package com.rust.exfil.takebradley.model.loot.ammo;

public class AmmoFactory {
  public static AmmoItem create(AmmoType type) {
    switch (type) {
      case PISTOL:
        return new PistolAmmo();
      case RIFLE:
        return new RifleAmmo();
      case ROCKET:
        return new RocketAmmo();
      default:
        throw new IllegalArgumentException("Unknown AmmoType: " + type);
    }
  }
}

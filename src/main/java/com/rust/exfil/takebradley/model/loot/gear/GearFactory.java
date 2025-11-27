package com.rust.exfil.takebradley.model.loot.gear;

public class GearFactory {
    public static GearItem create(GearType type) {
        switch (type) {
            case HAZMAT:
                return new HazmatGear();
            case ROADSIGN:
                return new RoadSignGear();
            case WOLFHEAD:
                return new WolfHeadGear();
            case HEAVYPOT:
                return new HeavyPotGear();
            default:
                throw new IllegalArgumentException("Unknown gear item type: " + type);
        }
    }
}

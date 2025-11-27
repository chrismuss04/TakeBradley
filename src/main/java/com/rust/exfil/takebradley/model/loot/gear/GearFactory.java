package com.rust.exfil.takebradley.model.loot.gear;

public class GearFactory {
    public HazmatGear createHazmatGear() {
        return new HazmatGear();
    }
    public WolfHeadGear createWolfHeadGear() {
        return new WolfHeadGear();
    }
    public RoadSignGear createRoadSignGear() {
        return new RoadSignGear();
    }
    public HeavyPotGear createHeavyPotGear() {
        return new HeavyPotGear();
    }
}

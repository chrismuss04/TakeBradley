package com.rust.exfil.takebradley.model.loot.gear;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GearFactoryTest {

    @Test
    void testCreateHazmatGear() {
        GearItem gear = GearFactory.create(GearType.HAZMAT);

        assertNotNull(gear);
        assertInstanceOf(HazmatGear.class, gear);
        assertEquals("Hazmat Suit", gear.getName());
        assertEquals(2, gear.getBuffs().size());
    }

    @Test
    void testCreateRoadSignGear() {
        GearItem gear = GearFactory.create(GearType.ROADSIGN);

        assertNotNull(gear);
        assertInstanceOf(RoadSignGear.class, gear);
        assertEquals("Road Sign Armor", gear.getName());
        assertEquals(2, gear.getBuffs().size());
    }

    @Test
    void testCreateWolfHeadGear() {
        GearItem gear = GearFactory.create(GearType.WOLFHEAD);

        assertNotNull(gear);
        assertInstanceOf(WolfHeadGear.class, gear);
        assertEquals("WolfHead Armor", gear.getName());
        assertEquals(2, gear.getBuffs().size());
    }

    @Test
    void testCreateHeavyPotGear() {
        GearItem gear = GearFactory.create(GearType.HEAVYPOT);

        assertNotNull(gear);
        assertInstanceOf(HeavyPotGear.class, gear);
        assertEquals("Heavy Pot", gear.getName());
        assertEquals(2, gear.getBuffs().size());
    }

    @Test
    void testAllGearTypesHaveFactoryImplementation() {
        for (GearType type : GearType.values()) {
            GearItem gear = GearFactory.create(type);
            assertNotNull(gear, "Factory should create gear for " + type);
        }
    }
}

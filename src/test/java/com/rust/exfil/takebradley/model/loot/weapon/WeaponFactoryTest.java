package com.rust.exfil.takebradley.model.loot.weapon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeaponFactoryTest {

    @Test
    void testCreateAK() {
        WeaponItem weapon = WeaponFactory.create(WeaponType.AK);

        assertNotNull(weapon);
        assertInstanceOf(AK.class, weapon);
        assertEquals("AK", weapon.getName());
        assertEquals(30, weapon.getMagazineSize());
        assertEquals(35, weapon.getDamage());
    }

    @Test
    void testCreateMP5() {
        WeaponItem weapon = WeaponFactory.create(WeaponType.MP5);

        assertNotNull(weapon);
        assertInstanceOf(MP5.class, weapon);
        assertEquals("MP5", weapon.getName());
        assertEquals(30, weapon.getMagazineSize());
        assertEquals(20, weapon.getDamage());
    }

    @Test
    void testCreateP2() {
        WeaponItem weapon = WeaponFactory.create(WeaponType.P2);

        assertNotNull(weapon);
        assertInstanceOf(P2.class, weapon);
        assertEquals("P2", weapon.getName());
        assertEquals(10, weapon.getMagazineSize());
        assertEquals(15, weapon.getDamage());
    }

    @Test
    void testCreateRocketLauncher() {
        WeaponItem weapon = WeaponFactory.create(WeaponType.ROCKET_LAUNCHER);

        assertNotNull(weapon);
        assertInstanceOf(RocketLauncher.class, weapon);
        assertEquals("Rocket Launcher", weapon.getName());
        assertEquals(1, weapon.getMagazineSize());
        assertEquals(100, weapon.getDamage());
    }

    @Test
    void testAllWeaponTypesHaveFactoryImplementation() {
        for (WeaponType type : WeaponType.values()) {
            WeaponItem weapon = WeaponFactory.create(type);
            assertNotNull(weapon, "Factory should create weapon for " + type);
        }
    }
}

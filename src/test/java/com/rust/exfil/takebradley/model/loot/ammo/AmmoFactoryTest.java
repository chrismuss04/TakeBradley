package com.rust.exfil.takebradley.model.loot.ammo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AmmoFactoryTest {

    @Test
    void testCreatePistolAmmo() {
        AmmoItem ammo = AmmoFactory.create(AmmoType.PISTOL);

        assertNotNull(ammo);
        assertInstanceOf(PistolAmmo.class, ammo);
        assertEquals("Pistol Ammo", ammo.getName());
    }

    @Test
    void testCreateRifleAmmo() {
        AmmoItem ammo = AmmoFactory.create(AmmoType.RIFLE);

        assertNotNull(ammo);
        assertInstanceOf(RifleAmmo.class, ammo);
        assertEquals("Rifle Ammo", ammo.getName());
    }

    @Test
    void testCreateRocketAmmo() {
        AmmoItem ammo = AmmoFactory.create(AmmoType.ROCKET);

        assertNotNull(ammo);
        assertInstanceOf(RocketAmmo.class, ammo);
        assertEquals("Rocket Ammo", ammo.getName());
    }

    @Test
    void testAllAmmoTypesHaveFactoryImplementation() {
        for (AmmoType type : AmmoType.values()) {
            AmmoItem ammo = AmmoFactory.create(type);
            assertNotNull(ammo, "Factory should create ammo for " + type);
        }
    }
}

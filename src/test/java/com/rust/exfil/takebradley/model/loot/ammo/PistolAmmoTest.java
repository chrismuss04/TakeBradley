package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PistolAmmoTest {

    private PistolAmmo pistolAmmo;

    @BeforeEach
    void setUp() {
        pistolAmmo = new PistolAmmo();
    }

    @Test
    void testPistolAmmoCreation() {
        assertNotNull(pistolAmmo);
    }

    @Test
    void testPistolAmmoIsAmmoItem() {
        assertInstanceOf(AmmoItem.class, pistolAmmo);
    }

    @Test
    void testPistolAmmoIsLootItem() {
        assertInstanceOf(LootItem.class, pistolAmmo);
    }

    @Test
    void testName() {
        assertEquals("Pistol Ammo", pistolAmmo.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "Standard pistol-caliber ammunition for use in handguns and submachine guns";
        assertEquals(expectedDesc, pistolAmmo.getDescription());
    }

    @Test
    void testUseMethod() {
        Entity entity = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        assertDoesNotThrow(() -> pistolAmmo.use(entity));
    }
}

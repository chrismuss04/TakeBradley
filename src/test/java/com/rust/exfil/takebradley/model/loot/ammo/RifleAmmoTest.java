package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RifleAmmoTest {

    private RifleAmmo rifleAmmo;

    @BeforeEach
    void setUp() {
        rifleAmmo = new RifleAmmo();
    }

    @Test
    void testRifleAmmoCreation() {
        assertNotNull(rifleAmmo);
    }

    @Test
    void testRifleAmmoIsAmmoItem() {
        assertInstanceOf(AmmoItem.class, rifleAmmo);
    }

    @Test
    void testRifleAmmoIsLootItem() {
        assertInstanceOf(LootItem.class, rifleAmmo);
    }

    @Test
    void testName() {
        assertEquals("Rifle Ammo", rifleAmmo.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "Rifle-caliber ammunition for use in rifles";
        assertEquals(expectedDesc, rifleAmmo.getDescription());
    }

    @Test
    void testUseMethod() {
        Entity entity = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        assertDoesNotThrow(() -> rifleAmmo.use(entity));
    }
}

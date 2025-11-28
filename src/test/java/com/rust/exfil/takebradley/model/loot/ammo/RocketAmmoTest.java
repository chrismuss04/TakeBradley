package com.rust.exfil.takebradley.model.loot.ammo;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RocketAmmoTest {

    private RocketAmmo rocketAmmo;

    @BeforeEach
    void setUp() {
        rocketAmmo = new RocketAmmo();
    }

    @Test
    void testRocketAmmoCreation() {
        assertNotNull(rocketAmmo);
    }

    @Test
    void testRocketAmmoIsAmmoItem() {
        assertInstanceOf(AmmoItem.class, rocketAmmo);
    }

    @Test
    void testRocketAmmoIsLootItem() {
        assertInstanceOf(LootItem.class, rocketAmmo);
    }

    @Test
    void testName() {
        assertEquals("Rocket Ammo", rocketAmmo.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "Ammunition for rocket launchers.";
        assertEquals(expectedDesc, rocketAmmo.getDescription());
    }

    @Test
    void testUseMethod() {
        Entity entity = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        assertDoesNotThrow(() -> rocketAmmo.use(entity));
    }
}

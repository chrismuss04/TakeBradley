package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AKTest {

    private AK ak;

    @BeforeEach
    void setUp() {
        ak = new AK();
    }

    @Test
    void testAKCreation() {
        assertNotNull(ak);
    }

    @Test
    void testAKIsWeaponItem() {
        assertInstanceOf(WeaponItem.class, ak);
    }

    @Test
    void testAKIsLootItem() {
        assertInstanceOf(LootItem.class, ak);
    }

    @Test
    void testName() {
        assertEquals("AK-47", ak.getName());
    }

    @Test
    void testDescription() {
        assertEquals("Assault rifle", ak.getDescription());
    }

    @Test
    void testMagazineSize() {
        assertEquals(30, ak.getMagazineSize());
    }

    @Test
    void testDamage() {
        assertEquals(35, ak.getDamage());
    }

    @Test
    void testUseMethod() {
        Entity entity = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        assertDoesNotThrow(() -> ak.use(entity));
    }

    @Test
    void testReloadMethod() {
        assertDoesNotThrow(() -> ak.reload());
    }
}

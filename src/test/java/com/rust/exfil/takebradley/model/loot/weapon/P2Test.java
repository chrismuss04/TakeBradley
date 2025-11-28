package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class P2Test {

    private P2 p2;

    @BeforeEach
    void setUp() {
        p2 = new P2();
    }

    @Test
    void testP2Creation() {
        assertNotNull(p2);
    }

    @Test
    void testP2IsWeaponItem() {
        assertInstanceOf(WeaponItem.class, p2);
    }

    @Test
    void testP2IsLootItem() {
        assertInstanceOf(LootItem.class, p2);
    }

    @Test
    void testName() {
        assertEquals("P2", p2.getName());
    }

    @Test
    void testDescription() {
        assertEquals("A semi-automatic pistol", p2.getDescription());
    }

    @Test
    void testMagazineSize() {
        assertEquals(10, p2.getMagazineSize());
    }

    @Test
    void testDamage() {
        assertEquals(15, p2.getDamage());
    }

    @Test
    void testUseMethod() {
        Entity entity = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        assertDoesNotThrow(() -> p2.use(entity));
    }

    @Test
    void testReloadMethod() {
        assertDoesNotThrow(() -> p2.reload());
    }
}

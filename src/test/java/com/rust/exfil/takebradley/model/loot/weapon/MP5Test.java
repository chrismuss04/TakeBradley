package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MP5Test {

    private MP5 mp5;

    @BeforeEach
    void setUp() {
        mp5 = new MP5();
    }

    @Test
    void testMP5Creation() {
        assertNotNull(mp5);
    }

    @Test
    void testMP5IsWeaponItem() {
        assertInstanceOf(WeaponItem.class, mp5);
    }

    @Test
    void testMP5IsLootItem() {
        assertInstanceOf(LootItem.class, mp5);
    }

    @Test
    void testName() {
        assertEquals("MP5", mp5.getName());
    }

    @Test
    void testDescription() {
        assertEquals("9mm submachine gun", mp5.getDescription());
    }

    @Test
    void testMagazineSize() {
        assertEquals(30, mp5.getMagazineSize());
    }

    @Test
    void testDamage() {
        assertEquals(20, mp5.getDamage());
    }

    @Test
    void testUseMethod() {
        Entity entity = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        assertDoesNotThrow(() -> mp5.use(entity));
    }

    @Test
    void testReloadMethod() {
        assertDoesNotThrow(() -> mp5.reload());
    }
}

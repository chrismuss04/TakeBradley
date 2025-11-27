package com.rust.exfil.takebradley.model.inventory;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponFactory;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StashTest {

    private Stash stash;
    private Entity owner;

    @BeforeEach
    void setUp() {
        owner = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        stash = new Stash(owner);
    }

    @Test
    void testStashCreation() {
        assertNotNull(stash);
        assertEquals(100, stash.getSize());
    }

    @Test
    void testStashIsInventory() {
        assertInstanceOf(Inventory.class, stash);
    }

    @Test
    void testDepositAllEmpty() {
        List<LootItem> items = new ArrayList<>();

        assertDoesNotThrow(() -> stash.depositAll(items));
    }

    @Test
    void testDepositAllWithItems() {
        List<LootItem> items = new ArrayList<>();
        items.add(WeaponFactory.create(WeaponType.AK));
        items.add(WeaponFactory.create(WeaponType.MP5));
        items.add(WeaponFactory.create(WeaponType.P2));

        stash.depositAll(items);

        assertNotNull(stash.getItem(0));
        assertNotNull(stash.getItem(1));
        assertNotNull(stash.getItem(2));
        assertEquals("AK-47", stash.getItem(0).getName());
        assertEquals("MP5", stash.getItem(1).getName());
        assertEquals("P2 Pistol", stash.getItem(2).getName());
    }

    @Test
    void testDepositAllToPartiallyFilled() {
        stash.addItem(WeaponFactory.create(WeaponType.ROCKET_LAUNCHER));

        List<LootItem> items = new ArrayList<>();
        items.add(WeaponFactory.create(WeaponType.AK));
        items.add(WeaponFactory.create(WeaponType.MP5));

        stash.depositAll(items);

        assertNotNull(stash.getItem(0));
        assertEquals("Rocket Launcher", stash.getItem(0).getName());
        assertNotNull(stash.getItem(1));
        assertEquals("AK-47", stash.getItem(1).getName());
        assertNotNull(stash.getItem(2));
        assertEquals("MP5", stash.getItem(2).getName());
    }

    @Test
    void testDepositAllWhenFull() {
        // Fill the stash
        for (int i = 0; i < 100; i++) {
            stash.addItem(WeaponFactory.create(WeaponType.AK));
        }

        List<LootItem> items = new ArrayList<>();
        items.add(WeaponFactory.create(WeaponType.MP5));

        stash.depositAll(items);

        // Stash should still be full with original items
        assertNotNull(stash.getItem(99));
    }

    @Test
    void testStashHasLargeCapacity() {
        for (int i = 0; i < 100; i++) {
            boolean result = stash.addItem(WeaponFactory.create(WeaponType.AK));
            assertTrue(result);
        }

        // Should be full now
        boolean result = stash.addItem(WeaponFactory.create(WeaponType.MP5));
        assertFalse(result);
    }

    @Test
    void testDepositAllManyItems() {
        List<LootItem> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(WeaponFactory.create(WeaponType.P2));
        }

        stash.depositAll(items);

        for (int i = 0; i < 50; i++) {
            assertNotNull(stash.getItem(i));
        }
        assertNull(stash.getItem(50));
    }
}

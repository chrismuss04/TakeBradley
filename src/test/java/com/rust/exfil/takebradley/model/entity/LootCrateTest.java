package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LootCrateTest {

    private LootCrate crate;

    @BeforeEach
    void setUp() {
        crate = new LootCrate("StandardCrate", 150.0, 200.0);
    }

    @Test
    void testLootCrateCreation() {
        assertNotNull(crate);
        assertEquals("StandardCrate", crate.getName());
        assertEquals(150.0, crate.getX());
        assertEquals(200.0, crate.getY());
    }

    @Test
    void testLootCrateDoesNotImplementCombatOrMovable() {
        assertFalse(crate instanceof Combatant);
        assertFalse(crate instanceof Movable);
    }

    @Test
    void testIsAlwaysDead() {
        assertFalse(crate.isAlive());
    }

    @Test
    void testInitialInventory() {
        Inventory inventory = crate.getInventory();
        assertNotNull(inventory);
        assertEquals(10, inventory.getSize());
    }

    @Test
    void testSetInventory() {
        Inventory newInventory = new Inventory(5, crate);
        crate.setInventory(newInventory);

        assertEquals(newInventory, crate.getInventory());
        assertEquals(5, crate.getInventory().getSize());
    }

    @Test
    void testPositionIsFinal() {
        assertEquals(150.0, crate.getX());
        assertEquals(200.0, crate.getY());
    }

    @Test
    void testUpdate() {
        assertDoesNotThrow(() -> crate.update(1.0));
    }

    @Test
    void testUniqueId() {
        LootCrate crate2 = new LootCrate("Crate2", 0, 0);
        assertNotEquals(crate.getId(), crate2.getId());
    }
}

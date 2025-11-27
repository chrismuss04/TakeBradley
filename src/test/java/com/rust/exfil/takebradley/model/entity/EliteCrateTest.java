package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EliteCrateTest {

    private EliteCrate crate;

    @BeforeEach
    void setUp() {
        crate = new EliteCrate("EliteCrate", 300.0, 400.0);
    }

    @Test
    void testEliteCrateCreation() {
        assertNotNull(crate);
        assertEquals("EliteCrate", crate.getName());
        assertEquals(300.0, crate.getX());
        assertEquals(400.0, crate.getY());
    }

    @Test
    void testEliteCrateDoesNotImplementCombatOrMovable() {
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
        Inventory newInventory = new Inventory(15, crate);
        crate.setInventory(newInventory);

        assertEquals(newInventory, crate.getInventory());
        assertEquals(15, crate.getInventory().getSize());
    }

    @Test
    void testPositionIsFinal() {
        assertEquals(300.0, crate.getX());
        assertEquals(400.0, crate.getY());
    }

    @Test
    void testUpdate() {
        assertDoesNotThrow(() -> crate.update(1.0));
    }

    @Test
    void testUniqueId() {
        EliteCrate crate2 = new EliteCrate("Crate2", 0, 0);
        assertNotEquals(crate.getId(), crate2.getId());
    }

    @Test
    void testDifferentFromLootCrate() {
        LootCrate normalCrate = new LootCrate("Normal", 0, 0);
        assertNotEquals(crate.getClass(), normalCrate.getClass());
    }
}

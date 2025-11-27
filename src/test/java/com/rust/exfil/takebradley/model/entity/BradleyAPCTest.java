package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BradleyAPCTest {

    private BradleyAPC bradley;

    @BeforeEach
    void setUp() {
        bradley = new BradleyAPC("Bradley Tank", 500.0, 600.0);
    }

    @Test
    void testBradleyCreation() {
        assertNotNull(bradley);
        assertEquals("Bradley Tank", bradley.getName());
        assertEquals(500.0, bradley.getX());
        assertEquals(600.0, bradley.getY());
        assertTrue(bradley.isAlive());
    }

    @Test
    void testBradleyImplementsInterfaces() {
        assertInstanceOf(Combatant.class, bradley);
        assertInstanceOf(Movable.class, bradley);
    }

    @Test
    void testInitialHealth() {
        assertEquals(1200, bradley.getHealth());
        assertEquals(1200, bradley.getMaxHealth());
    }

    @Test
    void testInitialSpeed() {
        assertEquals(2.0, bradley.getSpeed());
    }

    @Test
    void testInitialInventory() {
        Inventory inventory = bradley.getInventory();
        assertNotNull(inventory);
        assertEquals(1, inventory.getSize());
    }

    @Test
    void testTakeDamage() {
        bradley.takeDamage(200);
        assertEquals(1000, bradley.getHealth());
        assertTrue(bradley.isAlive());
    }

    @Test
    void testTakeDamageWithResistance() {
        bradley.setDamageResistance(0.60);
        bradley.takeDamage(500);

        // Damage should be 500 * (1 - 0.60) = 200
        assertEquals(1000, bradley.getHealth());
    }

    @Test
    void testTakeFatalDamage() {
        bradley.takeDamage(1500);

        assertEquals(0, bradley.getHealth());
        assertFalse(bradley.isAlive());
    }

    @Test
    void testHeal() {
        bradley.takeDamage(300);
        bradley.heal(150);

        assertEquals(1050, bradley.getHealth());
    }

    @Test
    void testHealDoesNotExceedMaxHealth() {
        bradley.takeDamage(100);
        bradley.heal(500);

        assertEquals(1200, bradley.getHealth());
    }

    @Test
    void testDie() {
        bradley.die();
        assertFalse(bradley.isAlive());
    }

    @Test
    void testMove() {
        bradley.move(5.0, 3.0);

        // New position should be 500 + (5 * 2) = 510, 600 + (3 * 2) = 606
        assertEquals(510.0, bradley.getX());
        assertEquals(606.0, bradley.getY());
    }

    @Test
    void testSetPosition() {
        bradley.setPosition(1000.0, 2000.0);

        assertEquals(1000.0, bradley.getX());
        assertEquals(2000.0, bradley.getY());
    }

    @Test
    void testSetSpeed() {
        bradley.setSpeed(3.0);
        assertEquals(3.0, bradley.getSpeed());
    }

    @Test
    void testDamageResistance() {
        assertEquals(0.0, bradley.getDamageResistance());

        bradley.setDamageResistance(0.80);
        assertEquals(0.80, bradley.getDamageResistance());
    }

    @Test
    void testSelectedSlotAlwaysZero() {
        assertEquals(0, bradley.getSelectedSlotIndex());
    }

    @Test
    void testUpdate() {
        assertDoesNotThrow(() -> bradley.update(1.0));
    }
}

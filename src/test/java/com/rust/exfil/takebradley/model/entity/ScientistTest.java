package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScientistTest {

    private Scientist scientist;

    @BeforeEach
    void setUp() {
        scientist = new Scientist("Dr. Johnson", 75.0, 125.0);
    }

    @Test
    void testScientistCreation() {
        assertNotNull(scientist);
        assertEquals("Dr. Johnson", scientist.getName());
        assertEquals(75.0, scientist.getX());
        assertEquals(125.0, scientist.getY());
        assertTrue(scientist.isAlive());
    }

    @Test
    void testScientistImplementsCombatantOnly() {
        assertInstanceOf(Combatant.class, scientist);
        assertFalse(scientist instanceof Movable);
    }

    @Test
    void testInitialHealth() {
        assertEquals(100, scientist.getHealth());
        assertEquals(100, scientist.getMaxHealth());
    }

    @Test
    void testInitialInventory() {
        Inventory inventory = scientist.getInventory();
        assertNotNull(inventory);
        assertEquals(5, inventory.getSize());
    }

    @Test
    void testPositionIsFinal() {
        // Position should not change (no move method available)
        assertEquals(75.0, scientist.getX());
        assertEquals(125.0, scientist.getY());
    }

    @Test
    void testTakeDamage() {
        scientist.takeDamage(30);
        assertEquals(70, scientist.getHealth());
        assertTrue(scientist.isAlive());
    }

    @Test
    void testTakeDamageWithResistance() {
        scientist.setDamageResistance(0.30);
        scientist.takeDamage(40);

        // Damage should be 40 * (1 - 0.30) = 28
        assertEquals(72, scientist.getHealth());
    }

    @Test
    void testTakeFatalDamage() {
        scientist.takeDamage(150);

        assertEquals(0, scientist.getHealth());
        assertFalse(scientist.isAlive());
    }

    @Test
    void testHeal() {
        scientist.takeDamage(35);
        scientist.heal(20);

        assertEquals(85, scientist.getHealth());
    }

    @Test
    void testHealDoesNotExceedMaxHealth() {
        scientist.takeDamage(15);
        scientist.heal(40);

        assertEquals(100, scientist.getHealth());
    }

    @Test
    void testDie() {
        scientist.die();
        assertFalse(scientist.isAlive());
    }

    @Test
    void testDamageResistance() {
        assertEquals(0.0, scientist.getDamageResistance());

        scientist.setDamageResistance(0.50);
        assertEquals(0.50, scientist.getDamageResistance());
    }

    @Test
    void testInitialSelectedSlot() {
        assertEquals(0, scientist.getSelectedSlotIndex());
    }

    @Test
    void testEquipItem() {
        scientist.equipItem(2);
        assertEquals(2, scientist.getSelectedSlotIndex());
    }

    @Test
    void testUpdate() {
        assertDoesNotThrow(() -> scientist.update(1.0));
    }
}

package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NpcPlayerTest {

    private NpcPlayer npc;

    @BeforeEach
    void setUp() {
        npc = new NpcPlayer("TestNPC", 50.0, 100.0);
    }

    @Test
    void testNpcCreation() {
        assertNotNull(npc);
        assertEquals("TestNPC", npc.getName());
        assertEquals(50.0, npc.getX());
        assertEquals(100.0, npc.getY());
        assertTrue(npc.isAlive());
    }

    @Test
    void testNpcImplementsInterfaces() {
        assertInstanceOf(Combatant.class, npc);
        assertInstanceOf(Movable.class, npc);
    }

    @Test
    void testInitialHealth() {
        assertEquals(100, npc.getHealth());
        assertEquals(100, npc.getMaxHealth());
    }

    @Test
    void testInitialSpeed() {
        assertEquals(4.0, npc.getSpeed());
    }

    @Test
    void testInitialInventory() {
        Inventory inventory = npc.getInventory();
        assertNotNull(inventory);
        assertEquals(8, inventory.getSize());
    }

    @Test
    void testTakeDamage() {
        npc.takeDamage(25);
        assertEquals(75, npc.getHealth());
        assertTrue(npc.isAlive());
    }

    @Test
    void testTakeDamageWithResistance() {
        npc.setDamageResistance(0.40);
        npc.takeDamage(50);

        // Damage should be 50 * (1 - 0.40) = 30
        assertEquals(70, npc.getHealth());
    }

    @Test
    void testTakeFatalDamage() {
        npc.takeDamage(120);

        assertEquals(0, npc.getHealth());
        assertFalse(npc.isAlive());
    }

    @Test
    void testHeal() {
        npc.takeDamage(40);
        npc.heal(25);

        assertEquals(85, npc.getHealth());
    }

    @Test
    void testHealDoesNotExceedMaxHealth() {
        npc.takeDamage(10);
        npc.heal(50);

        assertEquals(100, npc.getHealth());
    }

    @Test
    void testDie() {
        npc.die();
        assertFalse(npc.isAlive());
    }

    @Test
    void testMove() {
        npc.move(3.0, 2.0);

        // New position should be 50 + (3 * 4) = 62, 100 + (2 * 4) = 108
        assertEquals(62.0, npc.getX());
        assertEquals(108.0, npc.getY());
    }

    @Test
    void testSetPosition() {
        npc.setPosition(200.0, 300.0);

        assertEquals(200.0, npc.getX());
        assertEquals(300.0, npc.getY());
    }

    @Test
    void testSetSpeed() {
        npc.setSpeed(6.0);
        assertEquals(6.0, npc.getSpeed());
    }

    @Test
    void testDamageResistance() {
        assertEquals(0.0, npc.getDamageResistance());

        npc.setDamageResistance(0.25);
        assertEquals(0.25, npc.getDamageResistance());
    }

    @Test
    void testInitialSelectedSlot() {
        assertEquals(0, npc.getSelectedSlotIndex());
    }

    @Test
    void testEquipItem() {
        npc.equipItem(3);
        assertEquals(3, npc.getSelectedSlotIndex());
    }

    @Test
    void testUpdate() {
        assertDoesNotThrow(() -> npc.update(1.0));
    }
}

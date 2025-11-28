package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import com.rust.exfil.takebradley.model.inventory.Inventory;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponFactory;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "TestPlayer", 100.0, 200.0);
    }

    @Test
    void testPlayerCreation() {
        assertNotNull(player);
        assertEquals("TestPlayer", player.getName());
        assertEquals(100.0, player.getX());
        assertEquals(200.0, player.getY());
        assertTrue(player.isAlive());
    }

    @Test
    void testPlayerImplementsInterfaces() {
        assertInstanceOf(Combatant.class, player);
        assertInstanceOf(Movable.class, player);
    }

    @Test
    void testInitialHealth() {
        assertEquals(100, player.getHealth());
        assertEquals(100, player.getMaxHealth());
    }

    @Test
    void testInitialSpeed() {
        assertEquals(5.0, player.getSpeed());
    }

    @Test
    void testInitialInventory() {
        Inventory inventory = player.getInventory();
        assertNotNull(inventory);
        assertEquals(10, inventory.getSize());
    }

    @Test
    void testStashExists() {
        Stash stash = player.getStash();
        assertNotNull(stash);
        assertEquals(100, stash.getSize());
    }

    @Test
    void testTakeDamageWithoutResistance() {
        player.takeDamage(30);
        assertEquals(70, player.getHealth());
        assertTrue(player.isAlive());
    }

    @Test
    void testTakeDamageWithResistance() {
        player.setDamageResistance(0.50);
        player.takeDamage(40);

        // Damage should be 40 * (1 - 0.50) = 20
        assertEquals(80, player.getHealth());
    }

    @Test
    void testTakeFatalDamage() {
        player.takeDamage(150);

        assertEquals(0, player.getHealth());
        assertFalse(player.isAlive());
    }

    @Test
    void testTakeDamageDoesNotGoNegative() {
        player.takeDamage(200);
        assertEquals(0, player.getHealth());
    }

    @Test
    void testHeal() {
        player.takeDamage(50);
        player.heal(30);

        assertEquals(80, player.getHealth());
    }

    @Test
    void testHealDoesNotExceedMaxHealth() {
        player.takeDamage(20);
        player.heal(50);

        assertEquals(100, player.getHealth());
    }

    @Test
    void testHealAtFullHealth() {
        player.heal(20);
        assertEquals(100, player.getHealth());
    }

    @Test
    void testDie() {
        player.die();

        assertFalse(player.isAlive());
    }

    @Test
    void testMove() {
        player.move(2.0, 3.0);

        // New position should be 100 + (2 * 5) = 110, 200 + (3 * 5) = 215
        assertEquals(110.0, player.getX());
        assertEquals(215.0, player.getY());
    }

    @Test
    void testMoveWithNegativeDeltas() {
        player.move(-1.0, -2.0);

        // New position should be 100 + (-1 * 5) = 95, 200 + (-2 * 5) = 190
        assertEquals(95.0, player.getX());
        assertEquals(190.0, player.getY());
    }

    @Test
    void testSetPosition() {
        player.setPosition(50.0, 75.0);

        assertEquals(50.0, player.getX());
        assertEquals(75.0, player.getY());
    }

    @Test
    void testSetSpeed() {
        player.setSpeed(10.0);
        assertEquals(10.0, player.getSpeed());

        player.move(1.0, 0.0);
        assertEquals(110.0, player.getX());
    }

    @Test
    void testDamageResistance() {
        assertEquals(0.0, player.getDamageResistance());

        player.setDamageResistance(0.30);
        assertEquals(0.30, player.getDamageResistance());
    }

    @Test
    void testInitialSelectedSlot() {
        assertEquals(0, player.getSelectedSlotIndex());
    }

    @Test
    void testEquipItem() {
        player.equipItem(5);
        assertEquals(5, player.getSelectedSlotIndex());
    }

    @Test
    void testGetEquippedItemWhenEmpty() {
        LootItem equipped = player.getEquippedItem();
        assertNull(equipped);
    }

    @Test
    void testGetEquippedItemWithWeapon() {
        player.getInventory().addItem(WeaponFactory.create(WeaponType.AK));
        LootItem equipped = player.getEquippedItem();

        assertNotNull(equipped);
        assertEquals("AK", equipped.getName());
    }

    @Test
    void testExtract() {
        // Add items to inventory
        player.getInventory().addItem(WeaponFactory.create(WeaponType.AK));
        player.getInventory().addItem(WeaponFactory.create(WeaponType.MP5));

        player.extract();

        // Inventory should be empty
        assertNull(player.getInventory().getItem(0));
        assertNull(player.getInventory().getItem(1));

        // Stash should have items
        assertNotNull(player.getStash().getItem(0));
        assertNotNull(player.getStash().getItem(1));
        assertEquals("AK", player.getStash().getItem(0).getName());
        assertEquals("MP5", player.getStash().getItem(1).getName());
    }

    @Test
    void testUniqueId() {
        Player player2 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Player2", 0, 0);
        assertNotEquals(player.getId(), player2.getId());
    }

    @Test
    void testUpdate() {
        // Update method is empty, just ensure it doesn't throw
        assertDoesNotThrow(() -> player.update(1.0));
    }
}

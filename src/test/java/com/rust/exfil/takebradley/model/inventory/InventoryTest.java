package com.rust.exfil.takebradley.model.inventory;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.gear.GearFactory;
import com.rust.exfil.takebradley.model.loot.gear.GearType;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponFactory;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;
    private Entity owner;

    @BeforeEach
    void setUp() {
        owner = EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        inventory = new Inventory(5, owner);
    }

    @Test
    void testInventoryCreation() {
        assertNotNull(inventory);
        assertEquals(5, inventory.getSize());
    }

    @Test
    void testAddItemToEmptyInventory() {
        LootItem item = WeaponFactory.create(WeaponType.AK);
        boolean result = inventory.addItem(item);

        assertTrue(result);
        assertEquals(item, inventory.getItem(0));
    }

    @Test
    void testAddMultipleItems() {
        LootItem item1 = WeaponFactory.create(WeaponType.AK);
        LootItem item2 = WeaponFactory.create(WeaponType.MP5);

        assertTrue(inventory.addItem(item1));
        assertTrue(inventory.addItem(item2));

        assertEquals(item1, inventory.getItem(0));
        assertEquals(item2, inventory.getItem(1));
    }

    @Test
    void testAddItemWhenFull() {
        for (int i = 0; i < 5; i++) {
            inventory.addItem(WeaponFactory.create(WeaponType.AK));
        }

        boolean result = inventory.addItem(WeaponFactory.create(WeaponType.MP5));
        assertFalse(result);
    }

    @Test
    void testGetItemAtValidIndex() {
        LootItem item = WeaponFactory.create(WeaponType.P2);
        inventory.addItem(item);

        assertEquals(item, inventory.getItem(0));
    }

    @Test
    void testGetItemAtEmptySlot() {
        assertNull(inventory.getItem(2));
    }

    @Test
    void testGetItemAtInvalidIndex() {
        assertNull(inventory.getItem(10));
        assertNull(inventory.getItem(-1));
    }

    @Test
    void testRemoveItem() {
        LootItem item = WeaponFactory.create(WeaponType.AK);
        inventory.addItem(item);

        LootItem removed = inventory.removeItem(0);

        assertEquals(item, removed);
        assertNull(inventory.getItem(0));
    }

    @Test
    void testRemoveItemFromEmptySlot() {
        LootItem removed = inventory.removeItem(2);
        assertNull(removed);
    }

    @Test
    void testRemoveItemAtInvalidIndex() {
        assertNull(inventory.removeItem(10));
        assertNull(inventory.removeItem(-1));
    }

    @Test
    void testRemoveAllItems() {
        inventory.addItem(WeaponFactory.create(WeaponType.AK));
        inventory.addItem(WeaponFactory.create(WeaponType.MP5));
        inventory.addItem(WeaponFactory.create(WeaponType.P2));

        List<LootItem> removed = inventory.removeAllItems();

        assertEquals(3, removed.size());
        assertNull(inventory.getItem(0));
        assertNull(inventory.getItem(1));
        assertNull(inventory.getItem(2));
    }

    @Test
    void testRemoveAllItemsFromEmptyInventory() {
        List<LootItem> removed = inventory.removeAllItems();

        assertNotNull(removed);
        assertEquals(0, removed.size());
    }

    @Test
    void testIsValidIndex() {
        assertTrue(inventory.isValidIndex(0));
        assertTrue(inventory.isValidIndex(4));
        assertFalse(inventory.isValidIndex(5));
        assertFalse(inventory.isValidIndex(-1));
    }

    @Test
    void testEquipGear() {
        inventory.setEquippedGear(GearFactory.create(GearType.HAZMAT));

        assertNotNull(inventory.getEquippedGear());
        assertEquals("Hazmat Suit", inventory.getEquippedGear().getName());
    }

    @Test
    void testEquipGearAppliesBuffs() {
        Player player = (Player) owner;
        double initialSpeed = player.getSpeed();
        double initialResistance = player.getDamageResistance();

        inventory.setEquippedGear(GearFactory.create(GearType.HAZMAT));

        // Hazmat has +20% damage resist and -10% speed
        assertEquals(0.20, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed * 0.90, player.getSpeed(), 0.001);
    }

    @Test
    void testReplaceEquippedGear() {
        Player player = (Player) owner;
        inventory.setEquippedGear(GearFactory.create(GearType.HAZMAT));
        inventory.setEquippedGear(GearFactory.create(GearType.ROADSIGN));

        // RoadSign has +50% damage resist and -15% speed
        assertNotNull(inventory.getEquippedGear());
        assertEquals("Road Sign Armor", inventory.getEquippedGear().getName());
    }

    @Test
    void testRemoveEquippedGear() {
        Player player = (Player) owner;
        double initialSpeed = player.getSpeed();

        inventory.setEquippedGear(GearFactory.create(GearType.HAZMAT));
        LootItem removedGear = inventory.removeEquippedGear();

        assertNotNull(removedGear);
        assertEquals("Hazmat Suit", removedGear.getName());
        assertNull(inventory.getEquippedGear());

        // Buffs should be removed
        assertEquals(0.0, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed, player.getSpeed(), 0.001);
    }

    @Test
    void testRemoveEquippedGearWhenNoneEquipped() {
        LootItem removed = inventory.removeEquippedGear();
        assertNull(removed);
    }

    @Test
    void testAddItemToPartiallyFilledInventory() {
        inventory.addItem(WeaponFactory.create(WeaponType.AK));
        inventory.removeItem(0);
        inventory.addItem(WeaponFactory.create(WeaponType.MP5));

        // Should add to first empty slot (slot 0)
        assertNotNull(inventory.getItem(0));
        assertEquals("MP5", inventory.getItem(0).getName());
    }
}

package com.rust.exfil.takebradley.model.inventory;

import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class InventorySlotTest {

    @Mock
    private LootItem mockItem;

    private InventorySlot slot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slot = new InventorySlot();
    }

    @Test
    void testInitiallyEmpty() {
        assertTrue(slot.isEmpty());
        assertNull(slot.getItem());
    }

    @Test
    void testAddItemToEmptySlot() {
        boolean result = slot.addItem(mockItem);

        assertTrue(result);
        assertFalse(slot.isEmpty());
        assertEquals(mockItem, slot.getItem());
    }

    @Test
    void testAddItemToOccupiedSlot() {
        slot.addItem(mockItem);

        LootItem anotherItem = org.mockito.Mockito.mock(LootItem.class);
        boolean result = slot.addItem(anotherItem);

        assertFalse(result);
        assertEquals(mockItem, slot.getItem());
    }

    @Test
    void testRemoveItem() {
        slot.addItem(mockItem);
        LootItem removed = slot.removeItem();

        assertEquals(mockItem, removed);
        assertTrue(slot.isEmpty());
        assertNull(slot.getItem());
    }

    @Test
    void testRemoveItemFromEmptySlot() {
        LootItem removed = slot.removeItem();

        assertNull(removed);
        assertTrue(slot.isEmpty());
    }

    @Test
    void testGetItemDoesNotRemove() {
        slot.addItem(mockItem);
        LootItem retrieved = slot.getItem();

        assertEquals(mockItem, retrieved);
        assertFalse(slot.isEmpty());
        assertEquals(mockItem, slot.getItem());
    }
}

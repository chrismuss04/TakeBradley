package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HazmatGearTest {

    private HazmatGear hazmat;

    @BeforeEach
    void setUp() {
        hazmat = new HazmatGear();
    }

    @Test
    void testHazmatCreation() {
        assertNotNull(hazmat);
    }

    @Test
    void testHazmatIsGearItem() {
        assertInstanceOf(GearItem.class, hazmat);
    }

    @Test
    void testHazmatIsLootItem() {
        assertInstanceOf(LootItem.class, hazmat);
    }

    @Test
    void testName() {
        assertEquals("Hazmat Suit", hazmat.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "Hazmat suit, provides minmal damage resistance";
        assertEquals(expectedDesc, hazmat.getDescription());
    }

    @Test
    void testHasTwoBuffs() {
        assertEquals(2, hazmat.getBuffs().size());
    }

    @Test
    void testUseEquipsGear() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.getInventory().addItem(hazmat);

        hazmat.use(player);

        assertNotNull(player.getInventory().getEquippedGear());
        assertEquals("Hazmat Suit", player.getInventory().getEquippedGear().getName());
    }

    @Test
    void testBuffsAreAppliedWhenUsed() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();
        player.getInventory().addItem(hazmat);

        hazmat.use(player);

        // Hazmat has +20% damage resist
        assertEquals(0.20, player.getDamageResistance(), 0.001);

        // Hazmat has -10% speed
        assertEquals(initialSpeed * 0.90, player.getSpeed(), 0.001);
    }

    @Test
    void testApplyBuffsDirectly() {
        Player player =(Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        hazmat.applyBuffs(player);

        assertEquals(0.20, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed * 0.90, player.getSpeed(), 0.001);
    }

    @Test
    void testRemoveBuffs() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        hazmat.applyBuffs(player);
        hazmat.removeBuffs(player);

        assertEquals(0.0, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed, player.getSpeed(), 0.001);
    }
}

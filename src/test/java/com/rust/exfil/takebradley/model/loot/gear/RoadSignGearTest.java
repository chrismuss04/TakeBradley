package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoadSignGearTest {

    private RoadSignGear roadSign;

    @BeforeEach
    void setUp() {
        roadSign = new RoadSignGear();
    }

    @Test
    void testRoadSignCreation() {
        assertNotNull(roadSign);
    }

    @Test
    void testRoadSignIsGearItem() {
        assertInstanceOf(GearItem.class, roadSign);
    }

    @Test
    void testRoadSignIsLootItem() {
        assertInstanceOf(LootItem.class, roadSign);
    }

    @Test
    void testName() {
        assertEquals("Road Sign Armor", roadSign.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "Lightweight metal armor, offers decent protection.";
        assertEquals(expectedDesc, roadSign.getDescription());
    }

    @Test
    void testHasTwoBuffs() {
        assertEquals(2, roadSign.getBuffs().size());
    }

    @Test
    void testUseEquipsGear() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.getInventory().addItem(roadSign);

        roadSign.use(player);

        assertNotNull(player.getInventory().getEquippedGear());
        assertEquals("Road Sign Armor", player.getInventory().getEquippedGear().getName());
    }

    @Test
    void testBuffsAreAppliedWhenUsed() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();
        player.getInventory().addItem(roadSign);

        roadSign.use(player);

        // RoadSign has +50% damage resist
        assertEquals(0.50, player.getDamageResistance(), 0.001);

        // RoadSign has -15% speed
        assertEquals(initialSpeed * 0.85, player.getSpeed(), 0.001);
    }

    @Test
    void testApplyBuffsDirectly() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        roadSign.applyBuffs(player);

        assertEquals(0.50, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed * 0.85, player.getSpeed(), 0.001);
    }

    @Test
    void testRemoveBuffs() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        roadSign.applyBuffs(player);
        roadSign.removeBuffs(player);

        assertEquals(0.0, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed, player.getSpeed(), 0.001);
    }

    @Test
    void testStrongerThanHazmat() {
        HazmatGear hazmat = new HazmatGear();
        Player player1 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test1", 0, 0);
        Player player2 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test2", 0, 0);

        hazmat.applyBuffs(player1);
        roadSign.applyBuffs(player2);

        // RoadSign should provide more damage resistance
        assertTrue(player2.getDamageResistance() > player1.getDamageResistance());
    }
}

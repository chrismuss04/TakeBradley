package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeavyPotGearTest {

    private HeavyPotGear heavyPlate;

    @BeforeEach
    void setUp() {
        heavyPlate = new HeavyPotGear();
    }

    @Test
    void testHeavyPlateCreation() {
        assertNotNull(heavyPlate);
    }

    @Test
    void testHeavyPlateIsGearItem() {
        assertInstanceOf(GearItem.class, heavyPlate);
    }

    @Test
    void testHeavyPlateIsLootItem() {
        assertInstanceOf(LootItem.class, heavyPlate);
    }

    @Test
    void testName() {
        assertEquals("Heavy Pot", heavyPlate.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "Thick and sturdy, offers excellent protection at cost of speed.";
        assertEquals(expectedDesc, heavyPlate.getDescription());
    }

    @Test
    void testHasTwoBuffs() {
        assertEquals(2, heavyPlate.getBuffs().size());
    }

    @Test
    void testUseEquipsGear() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.getInventory().addItem(heavyPlate);

        heavyPlate.use(player);

        assertNotNull(player.getInventory().getEquippedGear());
        assertEquals("Heavy Pot", player.getInventory().getEquippedGear().getName());
    }

    @Test
    void testBuffsAreAppliedWhenUsed() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();
        player.getInventory().addItem(heavyPlate);

        heavyPlate.use(player);

        // HeavyPlate has +70% damage resist (highest)
        assertEquals(0.70, player.getDamageResistance(), 0.001);

        // HeavyPlate has -50% speed (highest penalty)
        assertEquals(initialSpeed * 0.50, player.getSpeed(), 0.001);
    }

    @Test
    void testApplyBuffsDirectly() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        heavyPlate.applyBuffs(player);

        assertEquals(0.70, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed * 0.50, player.getSpeed(), 0.001);
    }

    @Test
    void testRemoveBuffs() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        heavyPlate.applyBuffs(player);
        heavyPlate.removeBuffs(player);

        assertEquals(0.0, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed, player.getSpeed(), 0.001);
    }

    @Test
    void testHighestDamageResistance() {
        HazmatGear hazmat = new HazmatGear();
        RoadSignGear roadSign = new RoadSignGear();
        WolfHeadGear wolfHead = new WolfHeadGear();

        Player player1 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test1", 0, 0);
        Player player2 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test2", 0, 0);
        Player player3 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test3", 0, 0);
        Player player4 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test4", 0, 0);

        hazmat.applyBuffs(player1);
        roadSign.applyBuffs(player2);
        wolfHead.applyBuffs(player3);
        heavyPlate.applyBuffs(player4);

        // HeavyPlate should have the highest damage resistance
        assertTrue(player4.getDamageResistance() > player1.getDamageResistance());
        assertTrue(player4.getDamageResistance() > player2.getDamageResistance());
        assertTrue(player4.getDamageResistance() > player3.getDamageResistance());
    }

    @Test
    void testHighestSpeedPenalty() {
        HazmatGear hazmat = new HazmatGear();
        RoadSignGear roadSign = new RoadSignGear();
        WolfHeadGear wolfHead = new WolfHeadGear();

        Player player1 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test1", 0, 0);
        Player player2 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test2", 0, 0);
        Player player3 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test3", 0, 0);
        Player player4 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test4", 0, 0);

        hazmat.applyBuffs(player1);
        roadSign.applyBuffs(player2);
        wolfHead.applyBuffs(player3);
        heavyPlate.applyBuffs(player4);

        // HeavyPlate should have the lowest speed (highest penalty)
        assertTrue(player4.getSpeed() < player1.getSpeed());
        assertTrue(player4.getSpeed() < player2.getSpeed());
        assertTrue(player4.getSpeed() < player3.getSpeed());
    }

    @Test
    void testDamageReductionWithHeavyPlate() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.getInventory().addItem(heavyPlate);
        heavyPlate.use(player);

        player.takeDamage(100);

        // With 70% resistance, 100 damage becomes 30
        assertEquals(70, player.getHealth());
    }
}

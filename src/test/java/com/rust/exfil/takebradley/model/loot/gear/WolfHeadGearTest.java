package com.rust.exfil.takebradley.model.loot.gear;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WolfHeadGearTest {

    private WolfHeadGear wolfHead;

    @BeforeEach
    void setUp() {
        wolfHead = new WolfHeadGear();
    }

    @Test
    void testWolfHeadCreation() {
        assertNotNull(wolfHead);
    }

    @Test
    void testWolfHeadIsGearItem() {
        assertInstanceOf(GearItem.class, wolfHead);
    }

    @Test
    void testWolfHeadIsLootItem() {
        assertInstanceOf(LootItem.class, wolfHead);
    }

    @Test
    void testName() {
        assertEquals("WolfHead Armor", wolfHead.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "Wolf hide armor, offers moderate protection";
        assertEquals(expectedDesc, wolfHead.getDescription());
    }

    @Test
    void testHasTwoBuffs() {
        assertEquals(2, wolfHead.getBuffs().size());
    }

    @Test
    void testUseEquipsGear() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.getInventory().addItem(wolfHead);

        wolfHead.use(player);

        assertNotNull(player.getInventory().getEquippedGear());
        assertEquals("WolfHead Armor", player.getInventory().getEquippedGear().getName());
    }

    @Test
    void testBuffsAreAppliedWhenUsed() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();
        player.getInventory().addItem(wolfHead);

        wolfHead.use(player);

        // WolfHead has +40% damage resist
        assertEquals(0.40, player.getDamageResistance(), 0.001);

        // WolfHead has -5% speed (lowest penalty)
        assertEquals(initialSpeed * 0.95, player.getSpeed(), 0.001);
    }

    @Test
    void testApplyBuffsDirectly() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        wolfHead.applyBuffs(player);

        assertEquals(0.40, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed * 0.95, player.getSpeed(), 0.001);
    }

    @Test
    void testRemoveBuffs() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        double initialSpeed = player.getSpeed();

        wolfHead.applyBuffs(player);
        wolfHead.removeBuffs(player);

        assertEquals(0.0, player.getDamageResistance(), 0.001);
        assertEquals(initialSpeed, player.getSpeed(), 0.001);
    }

    @Test
    void testLowestSpeedPenalty() {
        HazmatGear hazmat = new HazmatGear();
        RoadSignGear roadSign = new RoadSignGear();

        Player player1 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test1", 0, 0);
        Player player2 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test2", 0, 0);
        Player player3 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test3", 0, 0);

        hazmat.applyBuffs(player1);
        roadSign.applyBuffs(player2);
        wolfHead.applyBuffs(player3);

        // WolfHead should have the highest speed (lowest penalty)
        assertTrue(player3.getSpeed() > player1.getSpeed());
        assertTrue(player3.getSpeed() > player2.getSpeed());
    }
}

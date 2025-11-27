package com.rust.exfil.takebradley.model.loot.medical;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BandageTest {

    private Bandage bandage;

    @BeforeEach
    void setUp() {
        bandage = new Bandage();
    }

    @Test
    void testBandageCreation() {
        assertNotNull(bandage);
    }

    @Test
    void testBandageIsMedicalItem() {
        assertInstanceOf(MedicalItem.class, bandage);
    }

    @Test
    void testBandageIsLootItem() {
        assertInstanceOf(LootItem.class, bandage);
    }

    @Test
    void testName() {
        assertEquals("Bandage", bandage.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "A simple bandage used to heal minor damage";
        assertEquals(expectedDesc, bandage.getDescription());
    }

    @Test
    void testUseOnCombatant() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(30);
        player.getInventory().addItem(bandage);

        int healthBefore = player.getHealth();
        bandage.use(player);

        assertEquals(healthBefore + 15, player.getHealth());
        assertNull(player.getInventory().getItem(0)); // Item should be removed
    }

    @Test
    void testUseHealsCorrectAmount() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(50);

        player.getInventory().addItem(bandage);
        bandage.use(player);

        assertEquals(65, player.getHealth()); // 100 - 50 + 15 = 65
    }

    @Test
    void testUseDoesNotOverheal() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(10);

        player.getInventory().addItem(bandage);
        bandage.use(player);

        assertEquals(100, player.getHealth()); // Should cap at max health
    }

    @Test
    void testUseRemovesFromInventory() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(20);
        player.getInventory().addItem(bandage);

        assertNotNull(player.getInventory().getItem(0));
        bandage.use(player);
        assertNull(player.getInventory().getItem(0));
    }
}

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

class SyringeTest {

    private Syringe syringe;

    @BeforeEach
    void setUp() {
        syringe = new Syringe();
    }

    @Test
    void testSyringeCreation() {
        assertNotNull(syringe);
    }

    @Test
    void testSyringeIsMedicalItem() {
        assertInstanceOf(MedicalItem.class, syringe);
    }

    @Test
    void testSyringeIsLootItem() {
        assertInstanceOf(LootItem.class, syringe);
    }

    @Test
    void testName() {
        assertEquals("Medical Syringe", syringe.getName());
    }

    @Test
    void testDescription() {
        String expectedDesc = "A medical syringe used to heal moderate damage";
        assertEquals(expectedDesc, syringe.getDescription());
    }

    @Test
    void testUseOnCombatant() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(60);
        player.getInventory().addItem(syringe);

        int healthBefore = player.getHealth();
        syringe.use(player);

        assertEquals(healthBefore + 40, player.getHealth());
        assertNull(player.getInventory().getItem(0)); // Item should be removed
    }

    @Test
    void testUseHealsCorrectAmount() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(70);

        player.getInventory().addItem(syringe);
        syringe.use(player);

        assertEquals(70, player.getHealth()); // 100 - 70 + 40 = 70
    }

    @Test
    void testUseDoesNotOverheal() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(20);

        player.getInventory().addItem(syringe);
        syringe.use(player);

        assertEquals(100, player.getHealth()); // Should cap at max health
    }

    @Test
    void testUseRemovesFromInventory() {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        player.takeDamage(50);
        player.getInventory().addItem(syringe);

        assertNotNull(player.getInventory().getItem(0));
        syringe.use(player);
        assertNull(player.getInventory().getItem(0));
    }

    @Test
    void testSyringeHealsMoreThanBandage() {
        Bandage bandage = new Bandage();
        Player player1 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test1", 0, 0);
        Player player2 = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test2", 0, 0);

        player1.takeDamage(50);
        player2.takeDamage(50);

        player1.getInventory().addItem(bandage);
        player2.getInventory().addItem(syringe);

        bandage.use(player1);
        syringe.use(player2);

        // Syringe heals 40, bandage heals 15
        assertTrue(player2.getHealth() > player1.getHealth());
        assertEquals(65, player2.getHealth());
        assertEquals(65, player1.getHealth());
    }
}

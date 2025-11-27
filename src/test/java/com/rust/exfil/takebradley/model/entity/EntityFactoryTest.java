package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityFactoryTest {

    @Test
    void testCreatePlayer() {
        Entity entity = EntityFactory.createEntity(EntityType.PLAYER, "TestPlayer", 10.0, 20.0);

        assertNotNull(entity);
        assertInstanceOf(Player.class, entity);
        assertEquals("TestPlayer", entity.getName());
        assertEquals(10.0, entity.getX());
        assertEquals(20.0, entity.getY());
        assertTrue(entity.isAlive());
    }

    @Test
    void testCreateNpcPlayer() {
        Entity entity = EntityFactory.createEntity(EntityType.NPC, "TestNPC", 30.0, 40.0);

        assertNotNull(entity);
        assertInstanceOf(NpcPlayer.class, entity);
        assertEquals("TestNPC", entity.getName());
        assertEquals(30.0, entity.getX());
        assertEquals(40.0, entity.getY());
    }

    @Test
    void testCreateScientist() {
        Entity entity = EntityFactory.createEntity(EntityType.SCIENTIST, "Dr. Smith", 50.0, 60.0);

        assertNotNull(entity);
        assertInstanceOf(Scientist.class, entity);
        assertEquals("Dr. Smith", entity.getName());
        assertEquals(50.0, entity.getX());
        assertEquals(60.0, entity.getY());
    }

    @Test
    void testCreateBradleyAPC() {
        Entity entity = EntityFactory.createEntity(EntityType.BRADLEY_APC, "Bradley", 100.0, 200.0);

        assertNotNull(entity);
        assertInstanceOf(BradleyAPC.class, entity);
        assertEquals("Bradley", entity.getName());
        assertEquals(100.0, entity.getX());
        assertEquals(200.0, entity.getY());
    }

    @Test
    void testCreateLootCrate() {
        Entity entity = EntityFactory.createEntity(EntityType.LOOT_CRATE, "Loot", 150.0, 250.0);

        assertNotNull(entity);
        assertInstanceOf(LootCrate.class, entity);
        assertEquals("Loot", entity.getName());
        assertEquals(150.0, entity.getX());
        assertEquals(250.0, entity.getY());
        assertFalse(entity.isAlive());
    }

    @Test
    void testCreateEliteCrate() {
        Entity entity = EntityFactory.createEntity(EntityType.ELITE_CRATE, "EliteLoot", 300.0, 400.0);

        assertNotNull(entity);
        assertInstanceOf(EliteCrate.class, entity);
        assertEquals("EliteLoot", entity.getName());
        assertEquals(300.0, entity.getX());
        assertEquals(400.0, entity.getY());
        assertFalse(entity.isAlive());
    }

    @Test
    void testCreateWithNegativeCoordinates() {
        Entity entity = EntityFactory.createEntity(EntityType.PLAYER, "NegativePlayer", -50.0, -100.0);

        assertNotNull(entity);
        assertEquals(-50.0, entity.getX());
        assertEquals(-100.0, entity.getY());
    }

    @Test
    void testCreateWithZeroCoordinates() {
        Entity entity = EntityFactory.createEntity(EntityType.NPC, "OriginNPC", 0.0, 0.0);

        assertNotNull(entity);
        assertEquals(0.0, entity.getX());
        assertEquals(0.0, entity.getY());
    }
}

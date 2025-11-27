package com.rust.exfil.takebradley.model.buff;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BuffFactoryTest {

    @Test
    void testCreateDamageResistBuff() {
        Buff buff = BuffFactory.create(BuffType.DAMAGE_RESIST, 0.25);

        assertNotNull(buff);
        assertInstanceOf(DamageResist.class, buff);
        assertEquals("Damage Resistance", buff.getName());
        assertTrue(buff.getDescription().contains("25"));
    }

    @Test
    void testCreateMovementSpeedBuff() {
        Buff buff = BuffFactory.create(BuffType.MOVEMENT_SPEED, -0.15);

        assertNotNull(buff);
        assertInstanceOf(MovementSpeed.class, buff);
        assertEquals("Movement Speed Decrease", buff.getName());
        assertTrue(buff.getDescription().contains("15"));
    }

    @Test
    void testCreateWithZeroPercentage() {
        Buff damageResist = BuffFactory.create(BuffType.DAMAGE_RESIST, 0.0);
        Buff movementSpeed = BuffFactory.create(BuffType.MOVEMENT_SPEED, 0.0);

        assertNotNull(damageResist);
        assertNotNull(movementSpeed);
    }

    @Test
    void testCreateWithNegativeDamageResist() {
        Buff buff = BuffFactory.create(BuffType.DAMAGE_RESIST, -0.10);

        assertNotNull(buff);
        assertInstanceOf(DamageResist.class, buff);
    }

    @Test
    void testCreateWithPositiveMovementSpeed() {
        Buff buff = BuffFactory.create(BuffType.MOVEMENT_SPEED, 0.20);

        assertNotNull(buff);
        assertInstanceOf(MovementSpeed.class, buff);
    }
}

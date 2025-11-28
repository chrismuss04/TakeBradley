package com.rust.exfil.takebradley.model.buff;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DamageResistTest {

    @Mock
    private Combatant mockCombatant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConstructorAndGetters() {
        DamageResist buff = new DamageResist(0.30);

        assertEquals("Damage Resistance", buff.getName());
        assertEquals("+30% damage resistance", buff.getDescription());
    }

    @Test
    void testApplyBuff() {
        DamageResist buff = new DamageResist(0.20);
        when(mockCombatant.getDamageResistance()).thenReturn(0.0);

        buff.apply(mockCombatant);

        verify(mockCombatant).setDamageResistance(0.20);
    }

    @Test
    void testApplyBuffAddsToExistingResistance() {
        DamageResist buff = new DamageResist(0.25);
        when(mockCombatant.getDamageResistance()).thenReturn(0.15);

        buff.apply(mockCombatant);

        verify(mockCombatant).setDamageResistance(0.40);
    }

    @Test
    void testRemoveBuff() {
        DamageResist buff = new DamageResist(0.20);
        when(mockCombatant.getDamageResistance()).thenReturn(0.50);

        buff.remove(mockCombatant);

        verify(mockCombatant).setDamageResistance(0.30);
    }

    @Test
    void testRemoveBuffFromZeroResistance() {
        DamageResist buff = new DamageResist(0.10);
        when(mockCombatant.getDamageResistance()).thenReturn(0.10);

        buff.remove(mockCombatant);

        verify(mockCombatant).setDamageResistance(0.0);
    }

    @Test
    void testNegativeResistancePercentage() {
        DamageResist buff = new DamageResist(-0.10);

        assertEquals("Damage Resistance", buff.getName());
        assertTrue(buff.getDescription().contains("-10"));
    }

    @Test
    void testZeroResistancePercentage() {
        DamageResist buff = new DamageResist(0.0);

        assertEquals("+0% damage resistance", buff.getDescription());
    }

    @Test
    void testHighResistancePercentage() {
        DamageResist buff = new DamageResist(0.95);
        when(mockCombatant.getDamageResistance()).thenReturn(0.0);

        buff.apply(mockCombatant);

        verify(mockCombatant).setDamageResistance(0.95);
    }
}

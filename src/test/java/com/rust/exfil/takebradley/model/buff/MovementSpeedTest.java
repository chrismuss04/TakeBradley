package com.rust.exfil.takebradley.model.buff;

import com.rust.exfil.takebradley.model.entity.interfaces.Combatant;
import com.rust.exfil.takebradley.model.entity.interfaces.Movable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovementSpeedTest {

    @Mock
    private Combatant mockCombatant;

    @Mock
    private MovableCombatant mockMovableCombatant;

    interface MovableCombatant extends Combatant, Movable {}

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConstructorAndGetters() {
        MovementSpeed buff = new MovementSpeed(-0.15);

        assertEquals("Movement Speed Decrease", buff.getName());
        assertEquals("-15% movement speed", buff.getDescription());
    }

    @Test
    void testApplyBuffToMovableCombatant() {
        MovementSpeed buff = new MovementSpeed(-0.20);
        when(mockMovableCombatant.getSpeed()).thenReturn(5.0);

        buff.apply(mockMovableCombatant);

        // Speed should be 5.0 * (1 + (-0.20)) = 5.0 * 0.80 = 4.0
        verify(mockMovableCombatant).setSpeed(4.0);
    }

    @Test
    void testRemoveBuffFromMovableCombatant() {
        MovementSpeed buff = new MovementSpeed(-0.25);
        when(mockMovableCombatant.getSpeed()).thenReturn(3.75);

        buff.remove(mockMovableCombatant);

        // Speed should be 3.75 / (1 + (-0.25)) = 3.75 / 0.75 = 5.0
        verify(mockMovableCombatant).setSpeed(5.0);
    }

    @Test
    void testPositiveSpeedModifier() {
        MovementSpeed buff = new MovementSpeed(0.25);
        when(mockMovableCombatant.getSpeed()).thenReturn(4.0);

        buff.apply(mockMovableCombatant);

        // Speed should be 4.0 * (1 + 0.25) = 4.0 * 1.25 = 5.0
        verify(mockMovableCombatant).setSpeed(5.0);
    }

    @Test
    void testZeroSpeedModifier() {
        MovementSpeed buff = new MovementSpeed(0.0);
        when(mockMovableCombatant.getSpeed()).thenReturn(5.0);

        buff.apply(mockMovableCombatant);

        // Speed should remain 5.0 * (1 + 0.0) = 5.0
        verify(mockMovableCombatant).setSpeed(5.0);
    }

    @Test
    void testLargeNegativeSpeedModifier() {
        MovementSpeed buff = new MovementSpeed(-0.50);
        when(mockMovableCombatant.getSpeed()).thenReturn(10.0);

        buff.apply(mockMovableCombatant);

        // Speed should be 10.0 * (1 + (-0.50)) = 10.0 * 0.50 = 5.0
        verify(mockMovableCombatant).setSpeed(5.0);
    }

    @Test
    void testDescriptionFormatting() {
        MovementSpeed negBuff = new MovementSpeed(-0.33);
        MovementSpeed posBuff = new MovementSpeed(0.25);

        assertTrue(negBuff.getDescription().contains("-33"));
        assertTrue(posBuff.getDescription().contains("25"));
    }
}

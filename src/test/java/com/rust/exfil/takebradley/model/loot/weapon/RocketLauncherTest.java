package com.rust.exfil.takebradley.model.loot.weapon;

import com.rust.exfil.takebradley.model.entity.EntityFactory;
import com.rust.exfil.takebradley.model.entity.EntityType;
import com.rust.exfil.takebradley.model.entity.Player;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.loot.LootItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RocketLauncherTest {

    private RocketLauncher rocketLauncher;

    @BeforeEach
    void setUp() {
        rocketLauncher = new RocketLauncher();
    }

    @Test
    void testRocketLauncherCreation() {
        assertNotNull(rocketLauncher);
    }

    @Test
    void testRocketLauncherIsWeaponItem() {
        assertInstanceOf(WeaponItem.class, rocketLauncher);
    }

    @Test
    void testRocketLauncherIsLootItem() {
        assertInstanceOf(LootItem.class, rocketLauncher);
    }

    @Test
    void testName() {
        assertEquals("Rocket Launcher", rocketLauncher.getName());
    }

    @Test
    void testDescription() {
        assertEquals("A powerful explosive weapon", rocketLauncher.getDescription());
    }

    @Test
    void testMagazineSize() {
        assertEquals(1, rocketLauncher.getMagazineSize());
    }

    @Test
    void testDamage() {
        assertEquals(100, rocketLauncher.getDamage());
    }

    @Test
    void testUseMethod() {
        Entity entity = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Test", 0, 0);
        assertDoesNotThrow(() -> rocketLauncher.use(entity));
    }

    @Test
    void testReloadMethod() {
        assertDoesNotThrow(() -> rocketLauncher.reload(1));
    }

    @Test
    void testHighestDamage() {
        AK ak = new AK();
        MP5 mp5 = new MP5();
        P2 p2 = new P2();

        assertTrue(rocketLauncher.getDamage() > ak.getDamage());
        assertTrue(rocketLauncher.getDamage() > mp5.getDamage());
        assertTrue(rocketLauncher.getDamage() > p2.getDamage());
    }

    @Test
    void testSmallestMagazine() {
        AK ak = new AK();
        MP5 mp5 = new MP5();
        P2 p2 = new P2();

        assertTrue(rocketLauncher.getMagazineSize() < ak.getMagazineSize());
        assertTrue(rocketLauncher.getMagazineSize() < mp5.getMagazineSize());
        assertTrue(rocketLauncher.getMagazineSize() < p2.getMagazineSize());
    }
}

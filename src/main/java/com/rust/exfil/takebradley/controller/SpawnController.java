package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.*;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoFactory;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;
import com.rust.exfil.takebradley.model.loot.gear.GearFactory;
import com.rust.exfil.takebradley.model.loot.gear.GearType;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponFactory;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponType;

import java.util.List;
import java.util.Random;

public class SpawnController {
    private final GameWorld gameWorld;
    private final Random random;

    public SpawnController(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.random = new Random();
    }

    public SpawnController(GameWorld gameWorld, long seed) {
        this.gameWorld = gameWorld;
        this.random = new Random(seed);
    }
    
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    // spawn player at x,y with loadout (if present)
    public Player spawnPlayer(double x, double y, List<LootItem> loadout) {
        Player player = (Player) EntityFactory.createEntity(EntityType.PLAYER, "Player", x, y);
        
        // transfer loadout items to player inventory, else start with nothing
        if (loadout != null) {
            for (LootItem item : loadout) {
                player.getInventory().addItem(item);
            }
        }
        else {
            // Give player starting loadout: AK with ammo
        WeaponItem ak = WeaponFactory.create(WeaponType.AK);
        AmmoItem rifleAmmo = AmmoFactory.create(AmmoType.RIFLE);
        rifleAmmo.setQuantity(120); // 4 magazines worth
        
        player.getInventory().addItem(ak);
        player.getInventory().addItem(rifleAmmo);
        }
        
        gameWorld.addEntity(player);
        gameWorld.setPlayer(player); // Track player reference
        return player;
    }

    // spawn npc player at x,y with randomized loadout
    public NpcPlayer spawnNPC(String name, double x, double y) {
        NpcPlayer npc = (NpcPlayer) EntityFactory.createEntity(EntityType.NPC, name, x, y);
        
        // randomize ammo type: pistol or rifle
        AmmoType ammo = random.nextBoolean() ? AmmoType.RIFLE : AmmoType.PISTOL;
        npc.getInventory().addItem(AmmoFactory.create(ammo));
        // match ammo type :
        if(ammo == AmmoType.RIFLE) {
            npc.getInventory().addItem(WeaponFactory.create(WeaponType.AK));
        }
        else {
            WeaponType weapon = random.nextBoolean() ? WeaponType.MP5 : WeaponType.P2;
            npc.getInventory().addItem(WeaponFactory.create(weapon));
        }
        
        // randomize gear type: Road Sign or Wolf Head
        GearType gearType = random.nextBoolean() ? GearType.ROADSIGN : GearType.WOLFHEAD;
        npc.getInventory().addItem(GearFactory.create(gearType));
        npc.getInventory().equipGear();
        
        gameWorld.addEntity(npc);
        return npc;
    }

    // spawn a scientist at x,y with randomized loadout
    public Scientist spawnScientist(String name, double x, double y) {
        Scientist scientist = (Scientist) EntityFactory.createEntity(EntityType.SCIENTIST, name, x, y);
        
        // Randomize weapon: P2 or MP5
        WeaponType weapon = random.nextBoolean() ? WeaponType.P2 : WeaponType.MP5;
        
        scientist.getInventory().addItem(WeaponFactory.create(weapon));
        scientist.getInventory().addItem(AmmoFactory.create(AmmoType.PISTOL));
        scientist.getInventory().addItem(GearFactory.create(GearType.HAZMAT));
        
        scientist.getInventory().equipGear();
        
        gameWorld.addEntity(scientist);
        return scientist;
    }

    // spawn bradley apc at x,y
    public BradleyAPC spawnBradley(String name, double x, double y) {
        BradleyAPC bradley = (BradleyAPC) EntityFactory.createEntity(EntityType.BRADLEY_APC, name, x, y);
        
        // equip with RL with nearly infinite ammo
        WeaponItem rocketLauncher = WeaponFactory.create(WeaponType.ROCKET_LAUNCHER);
        rocketLauncher.setCurrentAmmo(9999);
        bradley.getInventory().addItem(rocketLauncher);
        
        gameWorld.addEntity(bradley);
        return bradley;
    }

    // spawn loot crate at x,y
    public LootCrate spawnLootCrate(String name, double x, double y) {
        LootCrate crate = (LootCrate) EntityFactory.createEntity(EntityType.LOOT_CRATE, name, x, y);
        
        // handle loot population later
        
        gameWorld.addEntity(crate);
        return crate;
    }

    // spawn elite crate at x,y
    public EliteCrate spawnEliteCrate(String name, double x, double y) {
        EliteCrate eliteCrate = (EliteCrate) EntityFactory.createEntity(EntityType.ELITE_CRATE, name, x, y);
        
        // handle loot population later
        
        gameWorld.addEntity(eliteCrate);
        return eliteCrate;
    }
}

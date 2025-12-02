package com.rust.exfil.takebradley.controller;

import com.rust.exfil.takebradley.model.GameWorld;
import com.rust.exfil.takebradley.model.entity.*;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoFactory;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;
import com.rust.exfil.takebradley.model.loot.gear.GearFactory;
import com.rust.exfil.takebradley.model.loot.gear.GearType;
import com.rust.exfil.takebradley.model.loot.medical.MedFactory;
import com.rust.exfil.takebradley.model.loot.medical.MedType;
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
        WeaponItem weapon = WeaponFactory.create(WeaponType.AK);
        AmmoItem ammo1 = AmmoFactory.create(AmmoType.RIFLE);
        ammo1.setQuantity(150);
        
        player.getInventory().addItem(weapon);
        player.getInventory().addItem(ammo1);
        }
        
        gameWorld.addEntity(player);
        gameWorld.setPlayer(player); // Track player reference
        return player;
    }

    // spawn npc player at x,y with randomized loadout
    public NpcPlayer spawnNPC(String name, double x, double y) {
        NpcPlayer npc = (NpcPlayer) EntityFactory.createEntity(EntityType.NPC, name, x, y);
        
        // randomize ammo type: pistol or rifle
        AmmoType ammoType = random.nextBoolean() ? AmmoType.RIFLE : AmmoType.PISTOL;
        
        // match ammo type and give 2 separate magazine stacks
        if(ammoType == AmmoType.RIFLE) {
            npc.getInventory().addItem(WeaponFactory.create(WeaponType.AK));
            // add 2 separate stacks of 30 rounds each
            npc.getInventory().addItem(AmmoFactory.create(AmmoType.RIFLE));
            npc.getInventory().addItem(AmmoFactory.create(AmmoType.RIFLE));
        }
        else {
            WeaponType weaponType = random.nextBoolean() ? WeaponType.MP5 : WeaponType.P2;
            npc.getInventory().addItem(WeaponFactory.create(weaponType));
            
            if (weaponType == WeaponType.MP5) {
                // add 2 separate stacks of 30 rounds each
                npc.getInventory().addItem(AmmoFactory.create(AmmoType.PISTOL));
            } else {
                // P2 uses 10 round mags so add 2 separate stacks
                AmmoItem ammo1 = AmmoFactory.create(AmmoType.PISTOL);
                ammo1.setQuantity(20);
                npc.getInventory().addItem(ammo1);
            }
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
        
        // randomize weapon: P2 or MP5
        WeaponType weaponType = random.nextBoolean() ? WeaponType.P2 : WeaponType.MP5;
        
        scientist.getInventory().addItem(WeaponFactory.create(weaponType));
        
        // give 2 separate magazine stacks
        if (weaponType == WeaponType.MP5) {
            // add 2 separate stacks of 30 rounds each
            scientist.getInventory().addItem(AmmoFactory.create(AmmoType.PISTOL)); // Stack 1: 30 rounds
            scientist.getInventory().addItem(AmmoFactory.create(AmmoType.PISTOL)); // Stack 2: 30 rounds
        } else {
            // P2 uses 10 round mags - add 2 separate stacks
            AmmoItem ammo1 = AmmoFactory.create(AmmoType.PISTOL);
            ammo1.setQuantity(20);
            scientist.getInventory().addItem(ammo1);
        }
        
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
        
        int itemCount = 2 + random.nextInt(3); // 2, 3, or 4 items
        
        for (int i = 0; i < itemCount; i++) {
            int itemType = random.nextInt(4); // 0=weapon, 1=ammo, 2=medical, 3=gear
            
            switch (itemType) {
                case 0: // Weapon
                    WeaponType[] weapons = {WeaponType.P2, WeaponType.MP5};
                    crate.getInventory().addItem(
                        WeaponFactory.create(weapons[random.nextInt(weapons.length)])
                    );
                    break;
                    
                case 1: // Ammo
                    AmmoType[] ammoTypes = {AmmoType.PISTOL, AmmoType.RIFLE};
                    AmmoItem ammo = AmmoFactory.create(ammoTypes[random.nextInt(ammoTypes.length)]);
                    ammo.setQuantity(30 + random.nextInt(31)); // 30-60 rounds
                    crate.getInventory().addItem(ammo);
                    break;
                    
                case 2: // Medical
                    MedType[] medTypes = {
                        MedType.BANDAGE,
                        MedType.SYRINGE
                    };
                    crate.getInventory().addItem(MedFactory.create(medTypes[random.nextInt(medTypes.length)]));
                    break;
                    
                case 3: // Gear
                    GearType[] gearTypes = {GearType.HAZMAT, GearType.WOLFHEAD};
                    crate.getInventory().addItem(GearFactory.create(gearTypes[random.nextInt(gearTypes.length)]));
                    break;
            }
        }
        
        gameWorld.addEntity(crate);
        return crate;
    }

    // spawn elite crate at x,y
    public EliteCrate spawnEliteCrate(String name, double x, double y) {
        EliteCrate eliteCrate = (EliteCrate) EntityFactory.createEntity(EntityType.ELITE_CRATE, name, x, y);
        
        int itemCount = 3 + random.nextInt(3); // 3, 4, or 5 items
        
        for (int i = 0; i < itemCount; i++) {
            int itemType = random.nextInt(4); // 0=weapon, 1=ammo, 2=medical, 3=gear
            
            switch (itemType) {
                case 0: // High-tier weapons
                    WeaponType[] weapons = {WeaponType.AK, WeaponType.ROCKET_LAUNCHER};
                    eliteCrate.getInventory().addItem(WeaponFactory.create(weapons[random.nextInt(weapons.length)]));
                    break;
                    
                case 1: // Large ammo stacks
                    AmmoType[] ammoTypes = {AmmoType.RIFLE, AmmoType.ROCKET};
                    AmmoItem ammo = AmmoFactory.create(ammoTypes[random.nextInt(ammoTypes.length)]);
                    ammo.setQuantity(60 + random.nextInt(61)); // 60-120 rounds
                    eliteCrate.getInventory().addItem(ammo);
                    break;
                    
                case 2: // Medical (syringes preferred)
                    eliteCrate.getInventory().addItem(MedFactory.create(MedType.SYRINGE));
                    break;
                    
                case 3: // High-tier gear
                    GearType[] gearTypes = {GearType.HEAVYPOT, GearType.ROADSIGN};
                    eliteCrate.getInventory().addItem(GearFactory.create(gearTypes[random.nextInt(gearTypes.length)]));
                    break;
            }
        }
        
        gameWorld.addEntity(eliteCrate);
        return eliteCrate;
    }
}

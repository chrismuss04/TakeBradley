package com.rust.exfil.takebradley.systems.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoFactory;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoType;
import com.rust.exfil.takebradley.model.loot.gear.GearFactory;
import com.rust.exfil.takebradley.model.loot.gear.GearType;
import com.rust.exfil.takebradley.model.loot.medical.MedFactory;
import com.rust.exfil.takebradley.model.loot.medical.MedType;
import com.rust.exfil.takebradley.model.loot.medical.MedicalItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponFactory;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StashDeserializer {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    // deserializer from JSON back to stash items
    public static Stash deserialize(String filePath, Entity owner) {
        File file = new File(filePath);
        
        // return empty stash if file doesn't exist
        if (!file.exists()) {
            System.out.println("No saved stash found, creating new empty stash");
            return new Stash(owner);
        }
        
        try {
            // read file, create new stash and deserialize file into objects in stash
            @SuppressWarnings("unchecked")
            Map<String, Object> stashData = mapper.readValue(file, Map.class);
            
            Stash stash = new Stash(owner);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) stashData.get("items");
            
            if (items != null) {
                for (Map<String, Object> itemData : items) {
                    try {
                        LootItem item = deserializeItem(itemData);
                        if (item != null) {
                            // Add item to stash
                            stash.addItem(item);
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to deserialize item: " + e.getMessage());
                    }
                }
            }
            
            return stash;
            
        } catch (IOException e) {
            System.err.println("Failed to deserialize stash (corrupted file?): " + e.getMessage());
            e.printStackTrace();
            return new Stash(owner);
        }
    }
    
    // get a single object from the file
    private static LootItem deserializeItem(Map<String, Object> itemData) {
        String type = (String) itemData.get("type");
        
        if (type == null) {
            return null;
        }
        
        switch (type) {
            case "weapon":
                return deserializeWeapon(itemData);
                
            case "ammo":
                return deserializeAmmo(itemData);
                
            case "gear":
                return deserializeGear(itemData);
                
            case "medical":
                return deserializeMedical(itemData);
                
            default:
                System.err.println("Unknown item type: " + type);
                return null;
        }
    }
    
    private static WeaponItem deserializeWeapon(Map<String, Object> itemData) {
        String weaponTypeName = (String) itemData.get("weaponType");
        WeaponType weaponType = WeaponType.valueOf(weaponTypeName);
        
        WeaponItem weapon = WeaponFactory.create(weaponType);
        
        // Restore current ammo
        Integer currentAmmo = (Integer) itemData.get("currentAmmo");
        if (currentAmmo != null) {
            weapon.setCurrentAmmo(currentAmmo);
        }
        
        return weapon;
    }
    
    private static AmmoItem deserializeAmmo(Map<String, Object> itemData) {
        String ammoTypeName = (String) itemData.get("ammoType");
        AmmoType ammoType = AmmoType.valueOf(ammoTypeName);
        
        AmmoItem ammo = AmmoFactory.create(ammoType);
        
        // restore quantity
        Integer quantity = (Integer) itemData.get("quantity");
        if (quantity != null) {
            ammo.setQuantity(quantity);
        }
        
        return ammo;
    }
    
    private static LootItem deserializeGear(Map<String, Object> itemData) {
        String gearTypeName = (String) itemData.get("gearType");
        GearType gearType = GearType.valueOf(gearTypeName);
        
        return GearFactory.create(gearType);
    }
    
    private static LootItem deserializeMedical(Map<String, Object> itemData) {
        String medTypeName = (String) itemData.get("medType");
        MedType medType = MedType.valueOf(medTypeName);
        
        com.rust.exfil.takebradley.model.loot.medical.MedicalItem medItem = 
            (MedicalItem) MedFactory.create(medType);
        
        // restore quantity
        Integer quantity = (Integer) itemData.get("quantity");
        if (quantity != null) {
            medItem.setQuantity(quantity);
        }
        
        return medItem;
    }
}

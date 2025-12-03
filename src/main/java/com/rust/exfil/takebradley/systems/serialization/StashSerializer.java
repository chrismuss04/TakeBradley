package com.rust.exfil.takebradley.systems.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rust.exfil.takebradley.model.inventory.Stash;
import com.rust.exfil.takebradley.model.loot.LootItem;
import com.rust.exfil.takebradley.model.loot.ammo.AmmoItem;
import com.rust.exfil.takebradley.model.loot.gear.GearItem;
import com.rust.exfil.takebradley.model.loot.medical.MedicalItem;
import com.rust.exfil.takebradley.model.loot.weapon.WeaponItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StashSerializer {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    
    public static boolean serialize(Stash stash, String filePath) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // convert stash to JSON-friendly format
            Map<String, Object> stashData = new HashMap<>();
            stashData.put("capacity", stash.getSize());
            stashData.put("items", serializeItems(stash));
            
            // write to file
            mapper.writeValue(file, stashData);
            return true;
            
        } catch (IOException e) {
            System.err.println("Failed to serialize stash: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // serialize stash to JSON
    private static List<Map<String, Object>> serializeItems(Stash stash) {
        List<Map<String, Object>> items = new ArrayList<>();
        
        for (int i = 0; i < stash.getSize(); i++) {
            LootItem item = stash.getItem(i);
            if (item != null) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("slotIndex", i);
                itemData.put("name", item.getName());
                itemData.put("description", item.getDescription());
                
                // determine item type and add type-specific properties
                if (item instanceof WeaponItem) {
                    WeaponItem weapon = (WeaponItem) item;
                    itemData.put("type", "weapon");
                    itemData.put("weaponType", weapon.getWeaponType().name());
                    itemData.put("damage", weapon.getDamage());
                    itemData.put("magazineSize", weapon.getMagazineSize());
                    itemData.put("currentAmmo", weapon.getCurrentAmmo());
                    itemData.put("ammoType", weapon.getAmmoType().name());
                    
                } else if (item instanceof AmmoItem) {
                    AmmoItem ammo = (AmmoItem) item;
                    itemData.put("type", "ammo");
                    itemData.put("ammoType", ammo.getAmmoType().name());
                    itemData.put("quantity", ammo.getQuantity());
                    
                } else if (item instanceof GearItem) {
                    GearItem gear = (GearItem) item;
                    itemData.put("type", "gear");
                    itemData.put("gearType", gear.getGearType().name());
                    
                } else if (item instanceof MedicalItem) {
                    MedicalItem med = (MedicalItem) item;
                    itemData.put("type", "medical");
                    itemData.put("medType", med.getMedType().name());
                    itemData.put("quantity", med.getQuantity());
                }
                
                items.add(itemData);
            }
        }
        
        return items;
    }
}

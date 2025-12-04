package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.inventory.Inventory;

import java.util.UUID;

public class EliteCrate implements Entity {
    private final UUID id;
    private final String name;
    private final double x, y;
    private boolean isAlive;
    private Inventory inventory;
    private boolean isLooted;

    EliteCrate(String name, double x, double y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.x = x;
        this.y = y;
        this.isAlive = true; 
        this.inventory = new Inventory(10, this);
        this.isLooted = false;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void update(double deltaTime) {

    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public boolean isLooted() {
        return isLooted;
    }
    
    public void setLooted(boolean looted) {
        this.isLooted = looted;
        // mark crate as dead when looted so it's filtered out from rendering
        if (looted) {
            this.isAlive = false;
        }
    }
}

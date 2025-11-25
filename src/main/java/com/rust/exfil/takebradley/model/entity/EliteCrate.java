package com.rust.exfil.takebradley.model.entity;

import com.rust.exfil.takebradley.model.entity.interfaces.Entity;
import com.rust.exfil.takebradley.model.inventory.Inventory;

import java.util.UUID;

public class EliteCrate implements Entity {
    private final UUID id;
    private final String name;
    private int x;
    private int y;
    private boolean isAlive;

    private Inventory inventory;

    public EliteCrate(String name, int x, int y) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.x = x;
        this.y = y;
        this.isAlive = false;
        this.inventory = new Inventory(10);
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
        return false;
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
}

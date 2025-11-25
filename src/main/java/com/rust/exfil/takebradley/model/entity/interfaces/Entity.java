package com.rust.exfil.takebradley.model.entity.interfaces;

import java.util.UUID;

public interface Entity {
    UUID getId();
    String getName();
    double getX();
    double getY();
    boolean isAlive();
    void update(double deltaTime);
}

package com.rust.exfil.takebradley.model.entity.interfaces;

public interface Movable {
    void move(double dx, double dy);
    void setPosition(double x, double y);
    double getSpeed();
    void setSpeed(double speed);
}

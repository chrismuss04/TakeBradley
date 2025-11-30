package com.rust.exfil.takebradley.model;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final double dx;
    private final double dy;

    Direction(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
}

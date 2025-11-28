package com.rust.exfil.takebradley.model;

import com.rust.exfil.takebradley.model.map.GameMap;
import com.rust.exfil.takebradley.model.map.Wall;

import java.util.List;

public class GameWorld {
    private final GameMap map;

    public GameWorld(GameMap map) {
        this.map = map;
    }

    public GameMap getMap() {
        return map;
    }

    // return true if the (x,y) coords intersect with any wall
    public boolean checkWallCollision(double x, double y) {
        List<Wall> walls = map.getWalls();
        for (Wall wall : walls) {
            if (wall.intersects(x, y)) {
                return true;
            }
        }
        return false;
    }
}

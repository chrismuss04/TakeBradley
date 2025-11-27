package com.rust.exfil.takebradley.model.buff;

public class BuffFactory {
    public static Buff create(BuffType type, double percentage) {
        switch (type) {
            case DAMAGE_RESIST:
                return new DamageResist(percentage);
            case MOVEMENT_SPEED:
                return new MovementSpeed(percentage);
            default:
                throw new IllegalArgumentException("Unknown buff type: " + type);
        }

    }
}

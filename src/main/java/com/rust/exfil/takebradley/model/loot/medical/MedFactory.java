package com.rust.exfil.takebradley.model.loot.medical;

public class MedFactory {
    public static MedicalItem create(MedType type) {
        switch (type) {
            case BANDAGE:
                return new Bandage();
            case SYRINGE:
                return new Syringe();
            default:
                throw new IllegalArgumentException("Unknown medical item type: " + type);
        }
    }
}

package com.rust.exfil.takebradley.model.loot.medical;

public class MedFactory {
    public Bandage createBandage() {
        return new Bandage();
    }
    public Syringe createSyringe() {
        return new Syringe();
    }
}

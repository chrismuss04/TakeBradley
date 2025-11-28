package com.rust.exfil.takebradley.model.loot.medical;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedFactoryTest {

    @Test
    void testCreateBandage() {
        MedicalItem med = MedFactory.create(MedType.BANDAGE);

        assertNotNull(med);
        assertInstanceOf(Bandage.class, med);
        assertEquals("Bandage", med.getName());
    }

    @Test
    void testCreateSyringe() {
        MedicalItem med = MedFactory.create(MedType.SYRINGE);

        assertNotNull(med);
        assertInstanceOf(Syringe.class, med);
        assertEquals("Syringe", med.getName());
    }

    @Test
    void testAllMedTypesHaveFactoryImplementation() {
        for (MedType type : MedType.values()) {
            MedicalItem med = MedFactory.create(type);
            assertNotNull(med, "Factory should create medical item for " + type);
        }
    }
}

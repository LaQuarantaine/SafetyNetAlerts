package com.openclassrooms.safetyNet.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.MedicalRecord;
import com.openclassrooms.safetyNet.model.Person;
import com.openclassrooms.safetyNet.util.AgeUtil;

@ExtendWith(MockitoExtension.class)
public class PersonAgeServiceTest {

	@Mock
    private JsonDataStore dataStore;
	
	@InjectMocks
    private PersonAgeService personAgeService;

    private Person pierre;
    private Person luc;

    @BeforeEach
    void setup() {
        pierre = new Person("Pierre", "Colbert", "7 rue des Acaccias", "Paris", "75000", "0203040507", "pierre@gmail.com");
        luc = new Person("Luc", "Boullet", "8 rue des Acaccias", "Paris", "75000", "0405060708", "luc@gmail.com");
    }
    
    @Test
    @DisplayName("Retourne l'âge exact si le dossier médical est trouvé")
    void getAgeForPerson_shouldReturnCorrectAgeWhenRecordExists() {
        String birthdate = "01/01/2000"; 
        List<MedicalRecord> records = List.of(
            new MedicalRecord("Luc", "Boullet", birthdate, List.of(), List.of())
        );

        when(dataStore.getMedicalRecords()).thenReturn(records);

        Integer age = personAgeService.getAgeForPerson(luc);

        assertNotNull(age);
        assertEquals(AgeUtil.calculateAge(birthdate), age);
    }
    
    
    @Test
    @DisplayName("Retourne null si aucun dossier médical n'est trouvé")
    void getAgeForPerson_shouldReturnNullWhenRecordNotFound() {
        when(dataStore.getMedicalRecords()).thenReturn(List.of());

        Integer age = personAgeService.getAgeForPerson(pierre);

        assertNull(age);
    }
    
    
    @Test
    @DisplayName("Retourne true si la personne a moins de 18 ans")
    void isChild_shouldReturnTrueWhenPersonIsChild() {
        String childBirthdate = "01/01/2018"; 
        List<MedicalRecord> records = List.of(
            new MedicalRecord("Pierre", "Colbert", childBirthdate, List.of(), List.of())
        );

        when(dataStore.getMedicalRecords()).thenReturn(records);

        boolean result = personAgeService.isChild(pierre);

        assertTrue(result);
    }
    
    
    @Test
    @DisplayName("Retourne false si la personne est majeure")
    void isChild_shouldReturnFalseWhenPersonIsAdult() {
        String adultBirthdate = "01/01/1980";
        List<MedicalRecord> records = List.of(
            new MedicalRecord("Luc", "Boullet", adultBirthdate, List.of(), List.of())
        );

        when(dataStore.getMedicalRecords()).thenReturn(records);

        boolean result = personAgeService.isChild(luc);

        assertFalse(result);
    }
}

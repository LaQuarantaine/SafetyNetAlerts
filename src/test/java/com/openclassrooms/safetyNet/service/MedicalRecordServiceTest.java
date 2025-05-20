package com.openclassrooms.safetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.MedicalRecordDTO;
import com.openclassrooms.safetyNet.model.MedicalRecord;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

	@Mock
    private JsonDataStore dataStore;

    @Mock
    private JsonFileService jsonFileService;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private List<MedicalRecord> medicalRecords;

    @BeforeEach
    void setup() {
        medicalRecords = new ArrayList<>(List.of(
            new MedicalRecord("Patrice", "Gateau", "01/01/1990",
                    List.of("aspirin:100mg"), List.of("pollen")),
            new MedicalRecord("Alice", "Robert", "01/01/2000",
                    List.of("ibuprofen:200mg"), List.of("gluten"))
        ));
        when(dataStore.getMedicalRecords()).thenReturn(medicalRecords);
    }
    
    
    @Test
    @DisplayName("Retourne un dossier médical par prénom et nom")
    void getByName_shouldReturnRecordWhenFound() {
        Optional<MedicalRecord> result = medicalRecordService.getByName("Patrice", "Gateau");

        assertTrue(result.isPresent());
        assertEquals("Patrice", result.get().getFirstName());
    }
    
    
    @Test
    @DisplayName("Ajoute un nouveau dossier médical")
    void addMedicalRecord_shouldAddRecordWhenNotExists() {
        MedicalRecordDTO dto = new MedicalRecordDTO("Olivier", "Durand", "01/01/2000",
                List.of("ibuprofen:200mg"), List.of("gluten"));

        boolean result = medicalRecordService.addMedicalRecord(dto);

        assertTrue(result);
        assertEquals(3, medicalRecords.size());
        verify(jsonFileService).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("N'ajoute pas un dossier médical si la personne existe déjà")
    void addMedicalRecord_shouldNotAddWhenAlreadyExists() {
        MedicalRecordDTO dto = new MedicalRecordDTO("Patrice", "Gateau", "01/01/1990",
                List.of("aspirin:100mg"), List.of("pollen"));

        boolean result = medicalRecordService.addMedicalRecord(dto);

        assertFalse(result);
        verify(jsonFileService, never()).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("Met à jour un dossier médical existant avec les nouvelles informations")
    void updateMedicalRecord_shouldUpdateFieldsWhenExists() {
        MedicalRecordDTO dto = new MedicalRecordDTO("Patrice", "Gateau", "02/02/1991",
                List.of("paracetamol:500mg"), List.of("dust"));

        boolean result = medicalRecordService.updateMedicalRecord(dto);

        assertTrue(result);
        MedicalRecord updated = medicalRecords.get(0);
        assertEquals("02/02/1991", updated.getBirthdate());
        assertEquals(List.of("paracetamol:500mg"), updated.getMedications());
        assertEquals(List.of("dust"), updated.getAllergies());
        verify(jsonFileService).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("Ne met pas à jour un dossier médical si la personne est introuvable")
    void updateMedicalRecord_shouldReturnFalseIfNotFound() {
        MedicalRecordDTO dto = new MedicalRecordDTO("Olivier", "Durand", "01/01/2000",
                List.of(), List.of());

        boolean result = medicalRecordService.updateMedicalRecord(dto);

        assertFalse(result);
        verify(jsonFileService, never()).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("Supprime un dossier médical existant")
    void deleteMedicalRecord_shouldRemoveRecordWhenExists() {
        boolean result = medicalRecordService.deleteMedicalRecord("Alice", "Robert");

        assertTrue(result);
        assertFalse(medicalRecords.stream()
            .anyMatch(mr -> mr.getFirstName().equals("Alice") && mr.getLastName().equals("Robert")));
        verify(jsonFileService).saveDataToFile();
    }
    
    
    @Test
    @DisplayName("Ne supprime pas un dossier médical si la personne est introuvable")
    void deleteMedicalRecord_shouldReturnFalseWhenNotFound() {
        boolean result = medicalRecordService.deleteMedicalRecord("Olivier", "Durand");

        assertFalse(result);
        assertEquals(2, medicalRecords.size());
        verify(jsonFileService, never()).saveDataToFile();
    }
}

package com.openclassrooms.safetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;

@SpringBootTest
@ActiveProfiles("test")
public class FirestationLogicServiceIT {

	@Autowired
	private FirestationLogicService firestationLogicService;
	
	@Autowired
	private TestDataReloadService testDataReloadService;

	@BeforeEach
	void resetData() {
	    testDataReloadService.reloadData();
	}
	
	@Test
	@DisplayName("Retourne les résidents et le numéro de caserne pour une adresse donnée")
    void testGetFireAlert_shouldReturnCorrectResidentsAndStationNumber() {
        
        String address = "1509 Culver St";

        FireResponseDTO response = firestationLogicService.getFireAlert(address);

        assertNotNull(response, "La réponse ne doit pas être null");
        assertEquals("3", response.getStationNumber(), "Le numéro de caserne doit être 3");

        List<FireResidentDTO> residents = response.getResidents();
        assertNotNull(residents, "La liste de résidents ne doit pas être null");
        assertFalse(residents.isEmpty(), "Il doit y avoir au moins un résident");

        for (FireResidentDTO resident : residents) {
            assertNotNull(resident.getFirstName(), "Le prénom ne doit pas être null");
            assertNotNull(resident.getLastName(), "Le nom ne doit pas être null");
            assertNotNull(resident.getPhone(), "Le téléphone ne doit pas être null");
            assertNotNull(resident.getMedications(), "Les médicaments ne doivent pas être null");
            assertNotNull(resident.getAllergies(), "Les allergies ne doivent pas être null");
        }
    }
}

package com.openclassrooms.safetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.Firestation;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {


	    @Mock
	    private JsonDataStore dataStore;

	    @Mock
	    private JsonFileService jsonFileService;

	    @InjectMocks
	    private FirestationService firestationService;

	    private List<Firestation> firestations;

	    @BeforeEach
	    void setup() {
	        firestations = new ArrayList<>(List.of(
	            new Firestation("1 rue des Bois", "1"),
	            new Firestation("2 rue de la foret", "2"),
	            new Firestation("3 rue des Bois", "1") 
	        ));

	        when(dataStore.getFirestations()).thenReturn(firestations);
	    }
	    
	    
	    @Test
	    @DisplayName("Ajoute un nouveau mapping et sauvegarde les données")
	    void save_shouldAddMappingAndPersist() {
	        Firestation newMapping = new Firestation("4 rue Dune", "3");

	        Firestation result = firestationService.save(newMapping);

	        assertTrue(firestations.contains(newMapping));
	        assertEquals(newMapping, result);
	        verify(jsonFileService).saveDataToFile();
	    }
	    
	    
	    @Test
	    @DisplayName("Retourne les adresses couvertes par une caserne")
	    void getAddressesForStation_shouldReturnCorrectAddresses() {
	        Set<String> result = firestationService.getAddressesForStation("1");

	        assertEquals(Set.of("1 rue des Bois", "3 rue des Bois"), result);
	    }
	    
	    
	    @Test
	    @DisplayName("Modifie le numéro de caserne d’une adresse existante et sauvegarde")
	    void updateStationNumber_shouldUpdateAndPersistWhenAddressExists() {
	        List<Firestation> updated = firestationService.updateStationNumber("1 rue des Bois", "9");

	        assertEquals(1, updated.size());
	        assertEquals("9", updated.get(0).getStation());
	        verify(jsonFileService).saveDataToFile();
	    }
	    
	    
	    @Test
	    @DisplayName("Lève une exception si l’adresse à modifier est introuvable")
	    void updateStationNumber_shouldThrowWhenAddressNotFound() {
	        assertThrows(NoSuchElementException.class, () ->
	            firestationService.updateStationNumber("addresse inconnue", "9")
	        );

	        verify(jsonFileService).saveDataToFile(); 
	    }
	    
	    
	    @Test
	    @DisplayName("Supprime le mapping d’une adresse")
	    void deleteByAddress_shouldRemoveMappingAndPersist() {
	        firestationService.deleteByAddress("2 rue de la foret");

	        assertEquals(2, firestations.size());
	        assertTrue(firestations.stream().noneMatch(f -> f.getAddress().equals("2 rue de la foret")));
	        verify(jsonFileService).saveDataToFile();
	    }
	    
	    
	    @Test
	    @DisplayName("Supprime tous les mappings associés à une caserne")
	    void deleteByStation_shouldRemoveAllMatchingStations() {
	        firestationService.deleteByStation("1");

	        assertEquals(1, firestations.size()); 
	        assertTrue(firestations.stream().allMatch(f -> !f.getStation().equals("1")));
	        verify(jsonFileService).saveDataToFile();
	    }
	    
	    
	    @Test
	    @DisplayName("Retourne les numéros de caserne associés à une adresse")
	    void getStationNumbersByAddress_shouldReturnMatchingStations() {
	        Set<String> result = firestationService.getStationNumbersByAddress("1 rue des Bois");

	        assertEquals(Set.of("1"), result);
	    }
}

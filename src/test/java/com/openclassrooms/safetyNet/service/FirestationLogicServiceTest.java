package com.openclassrooms.safetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FirestationDTO;
import com.openclassrooms.safetyNet.dto.FirestationResponseDTO;
import com.openclassrooms.safetyNet.dto.PersonCoveredDTO;
import com.openclassrooms.safetyNet.model.Firestation;
import com.openclassrooms.safetyNet.model.MedicalRecord;
import com.openclassrooms.safetyNet.model.Person;

@ExtendWith(MockitoExtension.class)
public class FirestationLogicServiceTest {
	
	@Mock
	private JsonDataStore dataStore;
	
    @Mock
    private FirestationService firestationService;
    
    @Mock
    private PersonAccessService personAccessService;
    
    @Mock
    PersonAgeService personAgeService;
    
    @Mock
    private MedicalRecordService medicalRecordService;

	@InjectMocks
	private FirestationLogicService firestationLogicService;
	
	@Test
	@DisplayName("Retourne les personnes couvertes et leur répartition adulte/enfant")
    void getPersonsByStation_shouldReturnCorrectCountsAndDTOs() {
        // Given
        String station = "1";
        Set<String> addresses = Set.of("4 rue de la Sablière");

        Person person1 = new Person("Thierry", "Dupond", "4 rue de la Sablière", "Paris", "75000", "0101010101", "thierry@gmail.com");
        Person person2 = new Person("Emilie", "Dupond", "4 rue de la Sablière", "Paris", "75000", "0202020202", "emilie@gmail.com");

        when(firestationService.getAddressesForStation(station)).thenReturn(addresses);
        when(personAccessService.getResidentsAtAddresses(addresses)).thenReturn(List.of(person1, person2));

        when(personAgeService.getAgeForPerson(person1)).thenReturn(35);
        when(personAgeService.getAgeForPerson(person2)).thenReturn(8);

        when(personAgeService.isChild(person1)).thenReturn(false);
        when(personAgeService.isChild(person2)).thenReturn(true);

        // When
        FirestationResponseDTO response = firestationLogicService.getPersonsByStation("1");

        // Then
        assertEquals(1, response.getAdultCount());
        assertEquals(1, response.getChildCount());
        assertEquals(2, response.getPersons().size());

        PersonCoveredDTO dto1 = response.getPersons().get(0);
        PersonCoveredDTO dto2 = response.getPersons().get(1);

        assertTrue(
            (dto1.getFirstName().equals("Thierry") && dto1.getPhone().equals("0101010101")) ||
            (dto2.getFirstName().equals("Emilie") && dto2.getPhone().equals("0202020202"))
        );
    }
	
	
	@Test
	@DisplayName("Retourne les numéros de téléphone sans doublons pour une caserne")
	void getPhoneNumbersByStation_shouldReturnDistinctPhones() {
	    when(firestationService.getAddressesForStation("1")).thenReturn(Set.of("5 rue des Alpages"));
	    when(personAccessService.getResidentsAtAddresses(Set.of("5 rue des Alpages")))
	        .thenReturn(List.of(
	            new Person("Patrice", "Lambert", "5 rue des Alpages", "Paris", "75000", "0102030405", "patrice@gmail.com"),
	            new Person("Corinne", "Lambert", "5 rue des Alpages", "Paris", "75000", "0607080901", "corinne@gmail.com")
	        ));

	    List<String> phones = firestationLogicService.getPhoneNumbersByStation("1");

	    assertTrue(phones.size() == 2);
	    assertTrue(phones.contains("0102030405"));
	}

	
	@Test
	@DisplayName("Enregistre un nouveau mapping adresse/caserne à partir d’un DTO")
	void addFirestationMapping_shouldConvertDTOAndCallSave() {
	    FirestationDTO dto = new FirestationDTO("456 rue des Coquillages", "7");

	    firestationLogicService.addFirestationMapping(dto);

	    verify(firestationService).save(any(Firestation.class));
	}
	
	
	@Test
	@DisplayName("Regroupe les résidents par adresse pour plusieurs casernes")
	void getFloodInfoByStations_shouldGroupResidentsByAddress() {
	    Person person = new Person("Sandra", "Bullot", "7 rue des Coquillages", "Paris", "75000", "0405060780", "sandra@gmail.com");
	    MedicalRecord record = new MedicalRecord("Sandra", "Bullot", "01/01/1980", List.of("medicaments"), List.of("allergies"));

	    when(firestationService.getAddressesForStation("1")).thenReturn(Set.of("7 rue des Coquillages"));
	    when(personAccessService.getResidentsAtAddress("7 rue des Coquillages")).thenReturn(List.of(person));
	    when(medicalRecordService.getByName("Sandra", "Bullot")).thenReturn(Optional.of(record));

	    Map<String, List<FireResidentDTO>> result = firestationLogicService.getFloodInfoByStations(List.of("1"));

	    assertTrue(result.containsKey("7 rue des Coquillages"));
	    assertEquals(1, result.get("7 rue des Coquillages").size());
	    assertTrue(result.get("7 rue des Coquillages").get(0).getAllergies().contains("allergies"));
	}
	
	@Test
	@DisplayName("Supprime un mapping si l’adresse existe")
	void deleteFirestationByAddress_shouldReturnTrueWhenFound() {
	    Firestation station = new Firestation("1 rue Albert", "6");
	    when(dataStore.getFirestations()).thenReturn(List.of(station));

	    boolean result = firestationLogicService.deleteFirestationByAddress("1 rue Albert");

	    assertTrue(result);
	    verify(firestationService).deleteByAddress("1 rue Albert");
	}
	
	
	@Test
	@DisplayName("Ne fait rien si l’adresse à supprimer est introuvable")
	void deleteFirestationByAddress_shouldReturnFalseWhenNotFound() {
	    when(dataStore.getFirestations()).thenReturn(List.of());

	    boolean result = firestationLogicService.deleteFirestationByAddress("6 rue Albert");

	    assertFalse(result);
	    verify(firestationService, never()).deleteByAddress(any());
	}
	
	
	@Test
	@DisplayName("Supprime tous les mappings liés à une caserne")
	void deleteFirestationByStation_shouldReturnTrueIfMappingsExist() {
	    when(firestationService.getAddressesForStation("8")).thenReturn(Set.of("1 rue Belier"));

	    boolean result = firestationLogicService.deleteFirestationByStation("8");

	    assertTrue(result);
	    verify(firestationService).deleteByStation("8");
	}
	
	
	@Test
	@DisplayName("Met à jour le numéro de caserne pour une adresse")
	void updateFirestationMapping_shouldReturnTrueWhenUpdateSucceeds() {
	    
	    boolean result = firestationLogicService.updateFirestationMapping("1 rue C", "3");

	    assertTrue(result);
	    
	    verify(firestationService).updateStationNumber("1 rue C", "3");
	}

	
	@Test
	@DisplayName("Retourne faux si la mise à jour échoue pour une adresse inconnue")
	void updateFirestationMapping_shouldReturnFalseWhenExceptionThrown() {
	    doThrow(NoSuchElementException.class)
	        .when(firestationService).updateStationNumber("1 rue C", "3");

	    boolean result = firestationLogicService.updateFirestationMapping("1 rue C", "3");

	    assertFalse(result);
	}
}

package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.model.Firestation;
import com.openclassrooms.safetyNet.model.MedicalRecord;
import com.openclassrooms.safetyNet.model.Person;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PersonInfoServiceTest {

	@Mock private 
	JsonDataStore dataStore;
	@Mock 
	private MedicalRecordService medicalRecordService;
	@Mock
	private PersonAgeService personAgeService;
	@Mock
	private PersonAccessService personAccessService;

    @InjectMocks
    private PersonInfoService personInfoService;
    
    
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	@DisplayName("Retourne un enfant avec une liste de membres du foyer non vide")
	void getChildrenByAddress_shouldReturnChildrenWithHouseholdStructureOnly() {
	    // Arrange
	    Person child = new Person("Sara", "Dupont", "18 rue des Pivoines", "Paris", "75000", "0000", "sara@gmail.com");
	    Person adult1 = new Person("Suzanne", "Dupont", "18 rue des Pivoines", "Paris", "75000", "0000", "suzanne@x.com");
	    Person adult2 = new Person("Luc", "Dupont", "18 rue des Pivoines", "Paris", "75000", "0000", "luc@gmail.com");

	    List<Person> residents = List.of(child, adult1, adult2);
	    when(personAccessService.getResidentsAtAddress("18 rue des Pivoines")).thenReturn(residents);

	    when(personAgeService.getAgeForPerson(child)).thenReturn(10);
	    when(personAgeService.getAgeForPerson(adult1)).thenReturn(35);
	    when(personAgeService.getAgeForPerson(adult2)).thenReturn(40);

	    when(personAgeService.isChild(child)).thenReturn(true);
	    when(personAgeService.isChild(adult1)).thenReturn(false);
	    when(personAgeService.isChild(adult2)).thenReturn(false);

	    // Act
	    List<ChildAlertDTO> result = personInfoService.getChildrenByAddress("18 rue des Pivoines");

	    // Assert
	    assertThat(result).hasSize(1);
	    ChildAlertDTO dto = result.get(0);

	    assertThat(dto.getFirstName()).isEqualTo("Sara");
	    assertThat(dto.getHouseholdMembers()).isNotNull();
	    assertThat(dto.getHouseholdMembers()).hasSize(2); 
	}
	
	@Test
	@DisplayName("Retourne les résidents avec leurs infos médicales et le numéro de station")
	void getResidentsByAddressWithMedicalInfo_shouldReturnCorrectData() {
	    // Given

		String address = "18 rue des Pivoines";
		
	    Person person1 = new Person("Suzanne", "Dupont", address, "Paris", "75000", "0000", "suzanne@gmail.com");
	    Person person2 = new Person("Luc", "Dupont", address, "Paris", "75000", "0000", "luc@gmail.com");

	    MedicalRecord record = new MedicalRecord("Suzanne", "Dupont", "12/12/1972", List.of("Doliprane:500mg"), List.of("Peanuts"));
	    Firestation firestation = new Firestation("18 rue des Pivoines", "3");

	    // Mocks
	    when(personAccessService.getResidentsAtAddress(address)).thenReturn(List.of(person1, person2));
	    when(medicalRecordService.getByName("Suzanne", "Dupont")).thenReturn(Optional.of(record));
	    when(medicalRecordService.getByName("Luc", "Dupont")).thenReturn(Optional.empty());
	    when(dataStore.getFirestations()).thenReturn(List.of(firestation));

	    // When
	    FireResponseDTO response = personInfoService.getResidentsByAddressWithMedicalInfo(address);

	    // Then
	    assertThat(response.getStationNumber()).isEqualTo("3");
	    assertThat(response.getResidents()).hasSize(2);

	    FireResidentDTO resident = response.getResidents().get(0);
	    assertThat(resident.getFirstName()).isEqualTo("Suzanne");
	    assertThat(resident.getLastName()).isEqualTo("Dupont");
	    assertThat(resident.getPhone()).isEqualTo("0000");
	    assertThat(resident.getMedications()).containsExactly("Doliprane:500mg");
	    assertThat(resident.getAllergies()).containsExactly("Peanuts");
	}

}

package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.HouseholdMemberDTO;
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
	@DisplayName("Retourne uniquement les enfants vivant à l'adresse avec les autres membres du foyer")
	void getChildrenByAddress_shouldReturnChildrenWithHouseholdAndIgnoreExternalPerson() {
		// Arrange
	    Person person1 = new Person("Sara", "Dupont", "18 rue des Pivoines", "Paris", "75000", "0000", "sara@gmail.com");
	    Person person2 = new Person("Suzanne", "Dupont", "18 rue des Pivoines", "Paris", "75000", "0000", "suzanne@x.com");
	    Person person3 = new Person("Simon", "Robert", "123 avenue Leclerc", "Paris", "75000", "0000", "simon@gmail.com");     
	    Person person4 = new Person("Luc", "Dupont", "18 rue des Pivoines", "Paris", "75000", "0000", "luc@gmail.com");

	    List<Person> residentsAtAddress = List.of(person1, person2, person4);
	    when(personAccessService.getResidentsAtAddress("18 rue des Pivoines")).thenReturn(residentsAtAddress);

	    // Simuler les âges
	    when(personAgeService.getAgeForPerson(person1)).thenReturn(10);
	    when(personAgeService.getAgeForPerson(person2)).thenReturn(35);
	    when(personAgeService.getAgeForPerson(person4)).thenReturn(38);

	    // Simuler qui est enfant
	    when(personAgeService.isChild(person1)).thenReturn(true);
	    when(personAgeService.isChild(person2)).thenReturn(false);
	    when(personAgeService.isChild(person4)).thenReturn(false);

	    // Act
	    List<ChildAlertDTO> result = personInfoService.getChildrenByAddress("18 rue des Pivoines");

	    // Assert
	    assertThat(result).hasSize(1);
	    
	    assertThat(result).anySatisfy(dto -> {
	        assertThat(dto.getFirstName()).isEqualTo("Sara");
	        assertThat(dto.getLastName()).isEqualTo("Dupont");
	        assertThat(dto.getAge()).isEqualTo(10);
	        assertThat(dto.getHouseholdMembers())
	            .containsExactlyInAnyOrder(
	                new HouseholdMemberDTO("Suzanne", "Dupont"),
	                new HouseholdMemberDTO("Luc", "Dupont")
	            );
	    });
	    
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

package com.openclassrooms.safetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.CommunityEmailDTO;
import com.openclassrooms.safetyNet.dto.PersonInfoDTO;

@SpringBootTest
@ActiveProfiles("test")
public class PersonInfoServiceIT {

	@Autowired
	private PersonInfoService personInfoService;
	
	@Autowired
	private TestDataReloadService testDataReloadService;

	@BeforeEach
	void resetData() {
	    testDataReloadService.reloadData();
	}
	
	@Test
	@DisplayName("Retourne les enfants vivant à une adresse avec leur famille")
    void testGetChildrenByAddress_shouldReturnChildAlertDTO() {
		// GIVEN
	    String address = "1509 Culver St";
	    
	    // WHEN
		List<ChildAlertDTO> result = personInfoService.getChildrenByAddress(address);

		// THEN
		assertNotNull(result, "La liste retournée ne doit pas être nulle");
	    assertFalse(result.isEmpty(), "La liste ne doit pas être vide");
        
        for (ChildAlertDTO child : result) {
            // Vérifie qu’il s’agit bien d’un enfant
            assertTrue(child.getAge() < 18, "L’âge doit être inférieur à 18");

            // Vérifie que les membres du foyer ne contiennent pas l’enfant lui-même
            assertNotNull(child.getHouseholdMembers());

            boolean containsSelf = child.getHouseholdMembers().stream()
                .anyMatch(member ->
                    member.getFirstName().equals(child.getFirstName()) &&
                    member.getLastName().equals(child.getLastName())
                );

            assertFalse(containsSelf, "L’enfant ne doit pas apparaître dans la liste des autres membres du foyer");
            System.out.printf("Enfant : %s %s, autres membres : %s%n",
                    child.getFirstName(), child.getLastName(), child.getHouseholdMembers().size());
        }
	}
	
	
	@Test
	@DisplayName("Retourne les informations détaillées des personnes ayant un nom donné")
    void testGetPersonsByLastName_ShouldReturnPersonInfoDTO() {
        // GIVEN
        String lastName = "Stelzer";

        // WHEN
        List<PersonInfoDTO> result = personInfoService.getPersonsByLastName(lastName);

        // THEN
        assertNotNull(result);
        assertEquals(3, result.size());

        for (PersonInfoDTO dto : result) {
        	assertNotNull(dto.getFirstName());
        	assertEquals("Stelzer", dto.getLastName());
        	assertNotNull(dto.getAddress());
        	assertTrue(dto.getAge() > 0);
            assertNotNull(dto.getEmail());
            assertNotNull(dto.getMedications());
            assertNotNull(dto.getAllergies());
        }
	}
	
	
	@Test
	@DisplayName("Retourne les emails uniques des habitants d'une ville")
    void testGetEmailByCity_shouldReturnUniqueEmailsForCulver() {
        List<CommunityEmailDTO> emails = personInfoService.getEmailByCity("Culver");

        assertNotNull(emails);
        assertFalse(emails.isEmpty());

        // Tester le contenu avec une valeur connue
        assertTrue(
            emails.stream().anyMatch(e -> e.getEmail().equals("ssanw@email.com")),
            "L'email ssanw@email.com devrait être présent"
        );


        // Vérifie que tous les emails sont uniques (via un Set)
        Set<String> uniqueEmails = emails.stream()
            .map(CommunityEmailDTO::getEmail)
            .collect(Collectors.toSet());

        assertEquals(uniqueEmails.size(), emails.size(), "La liste des emails ne contient pas de doublons");
    }
}

package com.openclassrooms.safetyNet.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dto.PersonCreateDTO;
import com.openclassrooms.safetyNet.dto.PersonUpdateDTO;
import com.openclassrooms.safetyNet.service.TestDataReloadService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(PersonControllerIT.class);
	
    @Autowired
    private MockMvc mockMvc;	//simule des appels http aux controlleurs Spring MVC sans démarrer de serveur

    @Autowired
    private TestDataReloadService testDataReloadService;

    @BeforeEach
    void resetData() {
    	logger.info("[TEST] Données test copiées depuis src/test/resources vers target/test-classes");
        testDataReloadService.reloadData();
    }
    
  
    @Test
    @DisplayName(" Retourne les enfants avec les autres membres du foyer à l'adresse donnée")
    void getChildAlert_shouldReturnChildrenWithHouseholdMembers() throws Exception {
        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Vérifie qu’il y a bien 2 enfants
                .andExpect(jsonPath("$.length()").value(2))

                // Vérifie le premier enfant : Tenley
                .andExpect(jsonPath("$[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$[0].age").isNumber())		// vérifie que le champ est un nombre
                .andExpect(jsonPath("$[0].householdMembers.length()").value(3))

                // Vérifie le second enfant : Roger
                .andExpect(jsonPath("$[1].firstName").value("Roger"))
                .andExpect(jsonPath("$[1].age").isNumber())
                .andExpect(jsonPath("$[1].householdMembers.length()").value(3)); 
    }

    
    @Test
    @DisplayName("Retourne les résidents et numéro de caserne pour une adresse donnée")
    void getFireInfo_shouldReturnResidentsAtAddress() throws Exception {
        mockMvc.perform(get("/fire").param("address", "908 73rd St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents").isArray())
                .andExpect(jsonPath("$.residents.length()").value(2))
                .andExpect(jsonPath("$.residents[0].firstName").value("Reginold"))
                .andExpect(jsonPath("$.stationNumber").value("1"));
    }

    
    @Test
    @DisplayName("Retourne les informations détaillées des personnes avec un nom de famille donné")
    void getPersonInfo_shouldReturnMatchingPersons() throws Exception {
        mockMvc.perform(get("/personInfo").param("lastName", "Shepard"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1)) 
                .andExpect(jsonPath("$[0].firstName").value("Foster"))
                .andExpect(jsonPath("$[0].address").value("748 Townings Dr"))
                .andExpect(jsonPath("$[0].age").isNumber())
                .andExpect(jsonPath("$[0].email").value("jaboyd@email.com"))
                .andExpect(jsonPath("$[0].medications").isArray())
                .andExpect(jsonPath("$[0].allergies").isArray());
    }

    
    @Test
    @DisplayName("Retourne les adresses mail des habitants d'une ville")
    void getCommunityEmail_shouldReturnEmailsForCity() throws Exception {
        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(15));
    }

    
    @Test
    @DisplayName("Ajoute une personne")
    void postPerson_shouldReturnCreatedPerson() throws Exception {
        PersonCreateDTO dto = new PersonCreateDTO(
        	    "Catherine", "Deneuve", "14 rue Je ne sais pas", "Paris", "75000", "0101010101", "catherine.deneuve@gmail.com");

        String json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/person")
        	    .contentType(MediaType.APPLICATION_JSON)
        	    .content(json))
        	    .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Catherine"));
    }

    @Test
    @DisplayName("Met à jour une personne existante")
    void putPerson_shouldUpdatePersonIfExists() throws Exception {
    	PersonUpdateDTO updateDTO = new PersonUpdateDTO("26 Nouvelle rue", "Culver", "97400", "0102030405", "tony.cooper@gmail.com");

    	String json = new ObjectMapper().writeValueAsString(updateDTO);

        mockMvc.perform(put("/person")
                .param("firstName", "Tony")
                .param("lastName", "Cooper")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    
    @Test
    @DisplayName("Supprime une personne si elle existe")
    void deletePerson_shouldDeleteIfExists() throws Exception {
        mockMvc.perform(delete("/person")
                .param("firstName", "Peter")
                .param("lastName", "Duncan"))
                .andExpect(status().isOk());
    }
}

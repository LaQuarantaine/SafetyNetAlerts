package com.openclassrooms.safetyNet.controller;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FirestationDTO;
import com.openclassrooms.safetyNet.dto.FirestationResponseDTO;
import com.openclassrooms.safetyNet.dto.PersonCoveredDTO;
import com.openclassrooms.safetyNet.service.FirestationLogicService;

@WebMvcTest(FirestationController.class)
@ActiveProfiles("test")
public class FirestationControllerTest {


	    @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private FirestationLogicService firestationLogicService;

	    @Autowired
	    private ObjectMapper objectMapper;

	    @Test
	    @DisplayName("Retourne les personnes couvertes par une caserne avec le nombre d'adultes et d'enfants")
	    void getByStation_shouldReturnPersons() throws Exception {
	    	PersonCoveredDTO person = new PersonCoveredDTO("Ali", "Baba", "38 Cave des merveilles", "0101010101");
	    	FirestationResponseDTO response = new FirestationResponseDTO(List.of(person), 1, 0);
	    	
	        when(firestationLogicService.getPersonsByStation("1")).thenReturn(response);

	        mockMvc.perform(get("/firestation").param("stationNumber", "1"))
	               .andExpect(status().isOk())
	               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	               .andExpect(jsonPath("$.persons").isArray())
	               .andExpect(jsonPath("$.persons.length()").value(1))
	               .andExpect(jsonPath("$.persons[0].firstName").value("Ali"))
	               .andExpect(jsonPath("$.persons[0].lastName").value("Baba"))
	               .andExpect(jsonPath("$.persons[0].address").value("38 Cave des merveilles"))
	               .andExpect(jsonPath("$.persons[0].phone").value("0101010101"))
	               .andExpect(jsonPath("$.adultCount").value(1))
	               .andExpect(jsonPath("$.childCount").value(0));
	        
	        verify(firestationLogicService).getPersonsByStation("1");
	    }

	    
	    @Test
	    @DisplayName("Retourne la liste des numéros de téléphone pour une caserne donnée")
	    void getPhoneAlert_shouldReturnPhoneNumbers() throws Exception {
	        when(firestationLogicService.getPhoneNumbersByStation("1")).thenReturn(List.of("123456789"));

	        mockMvc.perform(get("/phoneAlert").param("firestation", "1"))
	               .andExpect(status().isOk())
	               .andExpect(jsonPath("$.length()").value(1));
	        
	        verify(firestationLogicService).getPhoneNumbersByStation("1");
	    }

	    
	    @Test
	    @DisplayName("Retourne une map adresse/résidents pour les casernes données")
	    void getFloodInfo_shouldReturnMapOfResidents() throws Exception {
	    	FireResidentDTO resident = new FireResidentDTO("Amanda", "Lear", "0101010101",List.of("med1", "med2"),List.of("pollen"));
	    	
	    	Map<String, List<FireResidentDTO>> result = Map.of("12 Rue Aristide Briand", List.of(resident));
	        when(firestationLogicService.getFloodInfoByStations(List.of("1", "2"))).thenReturn(result);

	        mockMvc.perform(get("/flood/stations").param("stations", "1,2"))
	               .andExpect(status().isOk())
	               .andExpect(jsonPath("$.['12 Rue Aristide Briand']").exists())
	               .andExpect(jsonPath("$.['12 Rue Aristide Briand'][0].firstName").value("Amanda"))
	               .andExpect(jsonPath("$.['12 Rue Aristide Briand'][0].lastName").value("Lear"))
	               .andExpect(jsonPath("$.['12 Rue Aristide Briand'][0].phone").value("0101010101"))
	               .andExpect(jsonPath("$.['12 Rue Aristide Briand'][0].medications[0]").value("med1"))
	               .andExpect(jsonPath("$.['12 Rue Aristide Briand'][0].allergies[0]").value("pollen"));
	        
	    }

	    
	    @Test
	    @DisplayName("Ajoute un nouveau mapping adresse/caserne")
	    void addMapping_shouldReturnOk() throws Exception {
	        FirestationDTO dto = new FirestationDTO("8 rue des Edelweiss", "4");

	        mockMvc.perform(post("/firestation")
	        		.contentType(MediaType.APPLICATION_JSON)
	        		.content(objectMapper.writeValueAsString(dto)))
	               .andExpect(status().isOk());

	        verify(firestationLogicService).addFirestationMapping(refEq(dto));	//vérifie que le controlleur appelle le bon service avec le bon argument
	    }

	    
	    @Test
	    @DisplayName("Met à jour le numéro de caserne pour une adresse")
	    void updateMapping_shouldReturnOkWhenUpdated() throws Exception {
	        when(firestationLogicService.updateFirestationMapping("8 rue des Edelweiss", "2")).thenReturn(true);

	        mockMvc.perform(put("/firestation")
	        		.param("address", "8 rue des Edelweiss")
	        		.param("newStation", "2"))
	               .andExpect(status().isOk());
	        
	        verify(firestationLogicService).updateFirestationMapping("8 rue des Edelweiss", "2");
	    }

	    
	    @Test
	    @DisplayName("Retourne 404 si l’adresse à mettre à jour n’existe pas")
	    void updateMapping_shouldReturnNotFoundWhenMissing() throws Exception {
	        when(firestationLogicService.updateFirestationMapping("inconnue", "2")).thenReturn(false);

	        mockMvc.perform(put("/firestation")
	                .param("address", "inconnue")
	                .param("newStation", "2"))
	               .andExpect(status().isNotFound());
	    }

	    
	    @Test
	    @DisplayName("Supprime un mapping adresse/caserne existant")
	    void deleteMapping_shouldReturnOkWhenDeleted() throws Exception {
	        when(firestationLogicService.deleteFirestationByAddress("1 Rue Jean Renoir")).thenReturn(true);

	        mockMvc.perform(delete("/firestation").param("address", "1 Rue Jean Renoir"))
	               .andExpect(status().isOk());
	    }

	    
	    @Test
	    @DisplayName("Retourne 404 si aucun mapping à supprimer n'existe pour l’adresse")
	    void deleteMapping_shouldReturnNotFoundWhenMissing() throws Exception {
	        when(firestationLogicService.deleteFirestationByAddress("inconnue")).thenReturn(false);

	        mockMvc.perform(delete("/firestation").param("address", "inconnue"))
	               .andExpect(status().isNotFound());
	    }


}

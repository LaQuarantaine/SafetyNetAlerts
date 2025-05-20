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
import com.openclassrooms.safetyNet.dto.MedicalRecordDTO;
import com.openclassrooms.safetyNet.service.TestDataReloadService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MedicalRecordControllerIT {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordControllerIT.class);
	
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TestDataReloadService testDataReloadService;

    @BeforeEach
    void resetData() {
    	logger.info("[TEST] Données test copiées depuis src/test/resources vers target/test-classes");
        testDataReloadService.reloadData();
        
    }
    
    @Test
    @DisplayName("Ajoute un dossier médical avec succès")
    void addMedicalRecord_shouldReturnOkIfCreated() throws Exception {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setMedications(List.of("aznol:200mg"));
        dto.setAllergies(List.of("peanuts"));
        
        mockMvc.perform(post("/medicalRecord")
                .param("firstName", "New")
                .param("lastName", "Person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
 
    }
}
    
  
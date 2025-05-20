package com.openclassrooms.safetyNet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dto.MedicalRecordDTO;
import com.openclassrooms.safetyNet.service.MedicalRecordService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.springframework.http.MediaType;

@WebMvcTest(MedicalRecordController.class)
@ActiveProfiles("test")
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Ajouter un dossier médical")
    void addMedicalRecord_shouldReturnOkWhenCreated() throws Exception {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setMedications(List.of("aspirin"));
        dto.setAllergies(List.of("pollen"));

        when(medicalRecordService.addMedicalRecord(Mockito.any())).thenReturn(true);

        mockMvc.perform(post("/medicalRecord")
                .param("firstName", "Alice")
                .param("lastName", "Merveille")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(medicalRecordService).addMedicalRecord(Mockito.any());
    }

    @Test
    @DisplayName("Renvoi une erreur si le dossier médical existe déjà")
    void addMedicalRecord_shouldReturnConflictWhenAlreadyExists() throws Exception {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        when(medicalRecordService.addMedicalRecord(Mockito.any())).thenReturn(false);

        mockMvc.perform(post("/medicalRecord")
                .param("firstName", "Farid")
                .param("lastName", "Bolide")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    
    @Test
    @DisplayName("Mettre à jour un dossier médical")
    void updateMedicalRecord_shouldReturnOkWhenUpdated() throws Exception {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        when(medicalRecordService.updateMedicalRecord(Mockito.any())).thenReturn(true);

        mockMvc.perform(put("/medicalRecord")
                .param("firstName", "Salima")
                .param("lastName", "Mavoisine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    
    @Test
    @DisplayName("Echec mise à jour si dossier médical introuvable")
    void updateMedicalRecord_shouldReturnNotFoundWhenMissing() throws Exception {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        when(medicalRecordService.updateMedicalRecord(Mockito.any())).thenReturn(false);

        mockMvc.perform(put("/medicalRecord")
                .param("firstName", "Harry")
                .param("lastName", "Escargot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Supprime un dossier médical")
    void deleteMedicalRecord_shouldReturnOkIfDeleted() throws Exception {
        when(medicalRecordService.deleteMedicalRecord("Marie", "Poppins")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                .param("firstName", "Marie")
                .param("lastName", "Poppins"))
                .andExpect(status().isOk());
    }

    
    @Test
    @DisplayName("Renvoi une erreur si dossier médical introuvable")
    void deleteMedicalRecord_shouldReturnNotFoundIfMissing() throws Exception {
        when(medicalRecordService.deleteMedicalRecord("Hulk", "Marvel")).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord")
                .param("firstName", "Hulk")
                .param("lastName", "Marvel"))
                .andExpect(status().isNotFound());
    }
    
}

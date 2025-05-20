package com.openclassrooms.safetyNet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.DataWrapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Profile("test")
public class TestDataReloadService {

    private final JsonDataStore dataStore;
    private final ObjectMapper objectMapper;

    @Value("classpath:data-test.json")
    private Resource dataFile;
  
    public TestDataReloadService(JsonDataStore dataStore, ObjectMapper objectMapper) {
        this.dataStore = dataStore;
        this.objectMapper = objectMapper;
    }

    
    public void reloadData() {
        try (InputStream is = dataFile.getInputStream()) {
            DataWrapper data = objectMapper.readValue(is, DataWrapper.class);
            dataStore.setPersons(data.getPersons());
            dataStore.setFirestations(data.getFirestations());
            dataStore.setMedicalRecords(data.getMedicalrecords());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du rechargement des données de test", e);
        }
    }
}

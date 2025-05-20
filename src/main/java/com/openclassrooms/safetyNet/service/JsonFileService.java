package com.openclassrooms.safetyNet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.DataWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
public class JsonFileService {

    private static final Logger log = LoggerFactory.getLogger(JsonFileService.class);

    private final JsonDataStore dataStore;
    private final ObjectMapper objectMapper;

    @Value("${safetynet.data-file}")
    private File dataFile;

    public JsonFileService(JsonDataStore dataStore, ObjectMapper objectMapper) {
        this.dataStore = dataStore;
        this.objectMapper = objectMapper;
    }
    
    public void saveDataToFile() {
        try {
            if (!dataFile.exists()) {
            	dataFile.getParentFile().mkdirs();
                Files.createFile(dataFile.toPath());
            }

            DataWrapper wrapper = new DataWrapper();
            wrapper.setPersons(dataStore.getPersons());
            wrapper.setFirestations(dataStore.getFirestations());
            wrapper.setMedicalrecords(dataStore.getMedicalRecords());

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, wrapper);
            log.info("Données enregistrées avec succès dans {}", dataFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du fichier JSON", e);
        }
    }
}
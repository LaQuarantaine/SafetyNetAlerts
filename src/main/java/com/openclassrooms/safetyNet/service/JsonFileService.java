package com.openclassrooms.safetyNet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.DataWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Service chargé de sauvegarder les données en mémoire vers data.json.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JsonFileService {

    private final JsonDataStore dataStore;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DATA_FILE_PATH = "src/main/resources/data.json";

    public void saveDataToFile() {
        try {
            DataWrapper wrapper = new DataWrapper();
            wrapper.setPersons(dataStore.getPersons());
            wrapper.setFirestations(dataStore.getFirestations());
            wrapper.setMedicalrecords(dataStore.getMedicalRecords());

            File file = new File(DATA_FILE_PATH);
            if (!file.exists()) {
                Files.createFile(Paths.get(DATA_FILE_PATH));
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, wrapper);
            log.info("Données enregistrées avec succès dans {}", DATA_FILE_PATH);

        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du fichier JSON", e);
        }
    }
}
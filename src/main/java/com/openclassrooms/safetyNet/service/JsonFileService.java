package com.openclassrooms.safetyNet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.DataWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Service chargé de sauvegarder les données en mémoire vers data.json.
 */
@Service
public class JsonFileService {

	private static final Logger log = LoggerFactory.getLogger(JsonFileService.class);
	
    private final JsonDataStore dataStore;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DATA_FILE_PATH = "src/main/resources/data.json";

    
    public JsonFileService(JsonDataStore dataStore) {
		super();
		this.dataStore = dataStore;
	}


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
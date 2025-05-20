package com.openclassrooms.safetyNet.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.DataWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TestDataLoader.class);

    private final JsonDataStore dataStore;
    private final ObjectMapper objectMapper;

    @Value("classpath:data-test.json")
    private Resource testDataFile;

    public TestDataLoader(JsonDataStore dataStore, ObjectMapper objectMapper) {
        this.dataStore = dataStore;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Chargement des données de test depuis {}", testDataFile.getFilename());
        try (InputStream is = testDataFile.getInputStream()) {
            DataWrapper data = objectMapper.readValue(is, DataWrapper.class);
            dataStore.setPersons(data.getPersons());
            dataStore.setFirestations(data.getFirestations());
            dataStore.setMedicalRecords(data.getMedicalrecords());
            log.info("Données de test chargées : {} personnes, {} casernes, {} dossiers médicaux",
                    data.getPersons().size(),
                    data.getFirestations().size(),
                    data.getMedicalrecords().size());
        }
    }
}

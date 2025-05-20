package com.openclassrooms.safetyNet.bootstrap;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.DataWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


@Component // Rend la classe détectable automatiquement par Spring
@Profile("!test") 
public class DataLoader implements CommandLineRunner {

		private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
		
		private final JsonDataStore dataStore;
		private final ObjectMapper objectMapper;
	
	    @Value("${safetynet.data-file}")
	    private File dataFile;
	    
	    
	    public DataLoader(JsonDataStore dataStore, ObjectMapper objectMapper) {
	        this.dataStore = dataStore;
	        this.objectMapper = objectMapper;
	    }


		//interface fonctionnelle (contient 1 seule méthode "run") SpringBoot 
	    // s'exécuter automatiquement au lancement de l'appli
	    
	    @Override
	    public void run(String... args) throws Exception {
	    	log.info("Chargement des données depuis {}", dataFile.getAbsolutePath());
	    	
	    	if (!dataFile.exists()) {
	            throw new RuntimeException("Fichier introuvable : " + dataFile.getAbsolutePath());
	        }
	    	
	    	// Création d'un ObjectMapper (outil de la bibliothèque Jackson)
	    	try (InputStream is = new FileInputStream(dataFile)) {
	    		DataWrapper data = objectMapper.readValue(is, DataWrapper.class);
	        
	    		dataStore.setPersons(data.getPersons());
	    		dataStore.setFirestations(data.getFirestations());
	    		dataStore.setMedicalRecords(data.getMedicalrecords());
	        
	        log.info("Données JSON chargées : {} personnes, {} casernes, {} dossiers médicaux",
	                data.getPersons().size(),
	                data.getFirestations().size(),
	                data.getMedicalrecords().size());
	    }
  
	}
}

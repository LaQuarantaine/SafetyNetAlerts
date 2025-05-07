package com.openclassrooms.safetyNet.bootstrap;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.DataWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
/**
 * Classe DataLoader
 * 
 * est automatiquement exécutée au démarrage de l'application,
 * grâce à l'implémentation de l'interface CommandLineRunner.
 *
 * Son rôle est de :
 * - Charger le fichier data.json situé dans le dossier resources
 * - Désérialiser le contenu JSON en objets Java via Jackson
 * - Insérer les données dans la base MySQL via les repositories Spring Data JPA
 *
 * Elle permet ainsi d'initialiser la base de données à chaque lancement,
 * ce qui est pratique en phase de développement ou de démonstration.
 */

@Component // Rend la classe détectable automatiquement par Spring
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

		private final JsonDataStore dataStore;
	

	    //interface fonctionnelle (contient 1 seule méthode "run") SpringBoot 
	    // permet d'exécuter du code automatiquement au lancement de l'appli
	    
	    @Override
	    public void run(String... args) throws Exception {
	    	log.info("Chargement des données depuis data.json...");
	    	
	    	// Création d'un ObjectMapper (outil de la bibliothèque Jackson)
	        ObjectMapper mapper = new ObjectMapper();
	        
	        // Chargement du fichier JSON depuis le dossier resources
	        InputStream is = getClass().getResourceAsStream("/data.json");

	        if (is == null) {
	            System.err.println("Le fichier data.json est introuvable.");
	            return;
	        }
	        
	        /*
	         * Lecture du fichier data.json avec Jacksonet conversion en un objet DataWrapper,
	         * Ce wrapper contient 3 listes : List<Person> persons, List<Firestation> firestations
	         * et List<MedicalRecord> medicalrecords
	         */
	        DataWrapper data = mapper.readValue(is, DataWrapper.class);

	        dataStore.setPersons(data.getPersons());
	        dataStore.setFirestations(data.getFirestations());
	        dataStore.setMedicalRecords(data.getMedicalrecords());
	        
	        log.info("Données JSON chargées en mémoire avec succès : {} personnes, {} casernes, {} dossiers médicaux",
	                data.getPersons().size(),
	                data.getFirestations().size(),
	                data.getMedicalrecords().size());
	    }
  
	}


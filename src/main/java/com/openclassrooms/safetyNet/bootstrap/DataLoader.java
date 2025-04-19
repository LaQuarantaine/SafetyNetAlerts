package com.openclassrooms.safetyNet.bootstrap;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.model.DataWrapper;
import com.openclassrooms.safetyNet.repository.FirestationRepository;
import com.openclassrooms.safetyNet.repository.MedicalRecordRepository;
import com.openclassrooms.safetyNet.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
public class DataLoader implements CommandLineRunner {

	
		// Injection des repositories Spring pour accéder aux tables
	    @Autowired private FirestationRepository firestationRepository;
	    @Autowired private MedicalRecordRepository medicalRecordRepository;
	    @Autowired private PersonRepository personRepository;

	    
	    
	    /*
	     * Méthode run() appelée automatiquement au démarrage de l'application.
	     * Elle contient la logique de lecture et d'insertion des données.
	    */
	

		
	    @Override
	    public void run(String... args) throws Exception {
	    	
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

	        // Supprime les données existantes pour éviter les doublons
	        personRepository.deleteAll();
	        medicalRecordRepository.deleteAll();
	        firestationRepository.deleteAll();

	        // Insère les nouvelles données via les repository
	        personRepository.saveAll(data.getPersons());
	        medicalRecordRepository.saveAll(data.getMedicalrecords());
	        firestationRepository.saveAll(data.getFirestations());

	        System.out.println("Données JSON insérées dans MySQL avec succès.");
      

	    }
	}


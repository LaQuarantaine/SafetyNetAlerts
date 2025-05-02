package com.openclassrooms.safetyNet.bootstrap;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.model.entity.DataWrapper;
import com.openclassrooms.safetyNet.repository.FirestationRepository;
import com.openclassrooms.safetyNet.repository.MedicalRecordRepository;
import com.openclassrooms.safetyNet.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Chargement des données initiales à partir du fichier data.json,
 * uniquement en environnement "dev" ou "test".
 */

@Component 					// Déclare un bean Spring
@Profile({"test"})	// Exécuté uniquement si le profil actif est "dev" ou "test"
public class DataLoader implements CommandLineRunner {

	//interface fonctionnelle (contient 1 seule méthode "run") SpringBoot 
	// permet d'exécuter du code automatiquement au lancement de l'appli
	
		// Injection des beans (ici les repositories)
	    @Autowired private FirestationRepository firestationRepository;
	    @Autowired private MedicalRecordRepository medicalRecordRepository;
	    @Autowired private PersonRepository personRepository;

	
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
	         * Lecture du fichier data.json avec Jackson et conversion en un objet DataWrapper,
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


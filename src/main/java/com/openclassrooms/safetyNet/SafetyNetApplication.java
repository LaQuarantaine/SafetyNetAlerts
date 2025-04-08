package com.openclassrooms.safetyNet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNet.model.DataWrapper;
import com.openclassrooms.safetyNet.repository.FirestationRepository;
import com.openclassrooms.safetyNet.repository.MedicalRecordRepository;
import com.openclassrooms.safetyNet.repository.PersonRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.InputStream;

@SpringBootApplication(scanBasePackages = "com.openclassrooms.safetyNet")
@EnableJpaRepositories(basePackages = "com.openclassrooms.safetyNet.repository")
@EntityScan(basePackages = "com.openclassrooms.safetyNet.model")
public class SafetyNetApplication implements CommandLineRunner{
	
	@Autowired 
	private FirestationRepository firestationRepository;
	
	@Autowired
	private MedicalRecordRepository medicalRecordRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SafetyNetApplication.class, args);
	}

	@Override
	public void run(String...args) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		InputStream is = getClass().getResourceAsStream("/data.json");
		
		if (is == null) {
	        System.err.println("Le fichier data.json est introuvable !");
	        return;
	    }
		
		DataWrapper data = mapper.readValue(is, DataWrapper.class);
		
		personRepository.saveAll(data.getPersons());
	    medicalRecordRepository.saveAll(data.getMedicalrecords());
	    firestationRepository.saveAll(data.getFirestations());

	    System.out.println("Données JSON insérées dans MySQL !");
	}

}

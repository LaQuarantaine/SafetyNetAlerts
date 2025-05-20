package com.openclassrooms.safetyNet.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.model.Person;
import com.openclassrooms.safetyNet.model.MedicalRecord;
import com.openclassrooms.safetyNet.util.AgeUtil;


@Service
public class PersonAgeService {
	
	private final JsonDataStore dataStore;
	
	public PersonAgeService(JsonDataStore dataStore) {
		super();
		this.dataStore = dataStore;
	}

	/**
     * Récupère l'âge d'une personne en consultant son dossier médical.
     * Retourne null si aucun dossier médical n'est trouvé.
     */
	public Integer getAgeForPerson(Person person) {
        return findMedicalRecord(person)
                .map(record -> AgeUtil.calculateAge(record.getBirthdate()))
                .orElse(null);
    }

    public boolean isChild(Person person) {
        return findMedicalRecord(person)
                .map(record -> AgeUtil.isChild(record.getBirthdate()))
                .orElse(false);
    }
    
    private Optional<MedicalRecord> findMedicalRecord(Person person) {
        return dataStore.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equals(person.getFirstName())
                        && record.getLastName().equals(person.getLastName()))
                .findFirst();
    }
}
	

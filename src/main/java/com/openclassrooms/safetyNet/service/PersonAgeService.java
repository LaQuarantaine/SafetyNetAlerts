package com.openclassrooms.safetyNet.service;

import org.springframework.stereotype.Service;

import com.openclassrooms.safetyNet.model.entity.Person;
import com.openclassrooms.safetyNet.repository.MedicalRecordRepository;
import com.openclassrooms.safetyNet.util.AgeUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonAgeService {
	
	private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Récupère l'âge d'une personne en consultant son dossier médical.
     * Retourne null si aucun dossier médical n'est trouvé.
     */
	public Integer getAgeForPerson(Person person) {
        return medicalRecordRepository.findByFirstNameAndLastName(
                person.getFirstName(), person.getLastName())
            .stream()
            .findFirst()
            .map(record -> AgeUtil.calculateAge(record.getBirthdate()))
            .orElse(0);
    }

    public boolean isChild(Person person) {
        return medicalRecordRepository.findByFirstNameAndLastName(
                person.getFirstName(), person.getLastName())
            .stream()
            .findFirst()
            .map(record -> AgeUtil.isChild(record.getBirthdate()))
            .orElse(false);
    }
}
	

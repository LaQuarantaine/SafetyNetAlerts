package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.model.view.PersonMedicalRecordView;
import com.openclassrooms.safetyNet.repository.PersonMedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonMedicalRecordService {

    private final PersonMedicalRecordRepository personMedicalRecordRepository;

    /**
     * Récupère un dossier médical à partir du prénom et nom.
     */
    public List<PersonMedicalRecordView> getByFirstAndLastName(String firstName, String lastName) {
        return personMedicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
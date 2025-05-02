package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.model.entity.MedicalRecord;
import com.openclassrooms.safetyNet.repository.MedicalRecordRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Récupère un dossier médical à partir du prénom et nom.
     */
    public Optional<MedicalRecord> getByName(String firstName, String lastName) {
        return medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName).stream().findFirst();
    }

   
}
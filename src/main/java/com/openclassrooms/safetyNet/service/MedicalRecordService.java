package com.openclassrooms.safetyNet.service;

import com.openclassrooms.safetyNet.dataStore.JsonDataStore;
import com.openclassrooms.safetyNet.dto.MedicalRecordDTO;
import com.openclassrooms.safetyNet.model.MedicalRecord;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    private final JsonDataStore dataStore;
    private final JsonFileService jsonFileService;

    public MedicalRecordService(JsonDataStore dataStore, JsonFileService jsonFileService) {
        this.dataStore = dataStore;
        this.jsonFileService = jsonFileService;
    }
    
    /**
     * Récupère un dossier médical à partir du prénom et nom.
     */
    public Optional<MedicalRecord> getByName(String firstName, String lastName) {
        return dataStore.getMedicalRecords().stream()
                .filter(record -> record.getFirstName().equals(firstName)
                        && record.getLastName().equals(lastName))
                .findFirst();
    }

    public boolean addMedicalRecord(MedicalRecordDTO dto) {
        boolean alreadyExists = getByName(dto.getFirstName(), dto.getLastName()).isPresent();
        if (alreadyExists) return false;

        MedicalRecord record = new MedicalRecord();
        record.setFirstName(dto.getFirstName());
        record.setLastName(dto.getLastName());
        record.setBirthdate(dto.getBirthdate());
        record.setMedications(dto.getMedications());
        record.setAllergies(dto.getAllergies());

        dataStore.getMedicalRecords().add(record);
        jsonFileService.saveDataToFile();
        return true;
    }

    public boolean updateMedicalRecord(MedicalRecordDTO dto) {
        Optional<MedicalRecord> optional = getByName(dto.getFirstName(), dto.getLastName());
        if (optional.isPresent()) {
            MedicalRecord record = optional.get();
            record.setBirthdate(dto.getBirthdate());
            record.setMedications(dto.getMedications());
            record.setAllergies(dto.getAllergies());
            jsonFileService.saveDataToFile();
            return true;
        }
        return false;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        List<MedicalRecord> list = dataStore.getMedicalRecords();
        boolean removed = list.removeIf(r -> r.getFirstName().equals(firstName)
                && r.getLastName().equals(lastName));
        if (removed) {
            jsonFileService.saveDataToFile();
        }
        return removed;
    }

}

package com.openclassrooms.safetyNet.dataStore;

import com.openclassrooms.safetyNet.model.Firestation;
import com.openclassrooms.safetyNet.model.MedicalRecord;
import com.openclassrooms.safetyNet.model.Person;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Conteneur mémoire central pour stocker toutes les données de l'application,
 * chargées depuis le fichier data.json au démarrage.
 */
@Data
@Component
public class JsonDataStore {

    private List<Person> persons = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
    private List<Firestation> firestations = new ArrayList<>();
}
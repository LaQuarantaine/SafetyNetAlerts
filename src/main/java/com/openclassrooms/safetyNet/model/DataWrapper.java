package com.openclassrooms.safetyNet.model;

import java.util.List;

import lombok.Data;
/**
 * Conteneur utilisé pour lire/écrire toutes les données de l'application
 * depuis ou vers le fichier data.json.
 */
@Data
public class DataWrapper {	
	private List<Person> persons;
	private List<Firestation> firestations;
	private List<MedicalRecord> medicalrecords;
}

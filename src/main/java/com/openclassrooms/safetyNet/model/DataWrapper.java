package com.openclassrooms.safetyNet.model;

import java.util.List;

/**
 * Conteneur utilisé pour lire/écrire toutes les données de l'application
 * depuis ou vers le fichier data.json.
 */

public class DataWrapper {	
	private List<Person> persons;
	private List<Firestation> firestations;
	private List<MedicalRecord> medicalrecords;
	
	
	public List<Person> getPersons() {
		return persons;
	}
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	public List<Firestation> getFirestations() {
		return firestations;
	}
	public void setFirestations(List<Firestation> firestations) {
		this.firestations = firestations;
	}
	public List<MedicalRecord> getMedicalrecords() {
		return medicalrecords;
	}
	public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
		this.medicalrecords = medicalrecords;
	}
	
	
}

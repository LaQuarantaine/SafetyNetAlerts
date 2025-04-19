package com.openclassrooms.safetyNet.model;

import java.util.List;

import lombok.Data;

@Data
public class DataWrapper {	//permet de lire une fois au démarrage toutes les données du fichier JSON pour les charger dans la base
	private List<Person> persons;
	private List<Firestation> firestations;
	private List<MedicalRecord> medicalrecords;
}

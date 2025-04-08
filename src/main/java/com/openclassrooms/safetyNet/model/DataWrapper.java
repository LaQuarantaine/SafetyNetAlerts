package com.openclassrooms.safetyNet.model;

import java.util.List;

import lombok.Data;

@Data
public class DataWrapper {
	private List<Person> persons;
	private List<Firestation> firestations;
	private List<MedicalRecord> medicalrecords;
}

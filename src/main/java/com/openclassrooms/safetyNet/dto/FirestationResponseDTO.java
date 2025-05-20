package com.openclassrooms.safetyNet.dto;

import java.util.List;


public class FirestationResponseDTO {
	private List<PersonCoveredDTO> persons;
	private int adultCount;
	private int childCount;
	
	
	public FirestationResponseDTO(List<PersonCoveredDTO> persons, int adultCount, int childCount) {
		super();
		this.persons = persons;
		this.adultCount = adultCount;
		this.childCount = childCount;
	}
	
	public List<PersonCoveredDTO> getPersons() {
		return persons;
	}
	public void setPersons(List<PersonCoveredDTO> persons) {
		this.persons = persons;
	}
	public int getAdultCount() {
		return adultCount;
	}
	public void setAdultCount(int adultCount) {
		this.adultCount = adultCount;
	}
	public int getChildCount() {
		return childCount;
	}
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

}



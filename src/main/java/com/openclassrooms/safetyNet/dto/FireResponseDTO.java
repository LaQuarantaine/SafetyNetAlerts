package com.openclassrooms.safetyNet.dto;

import java.util.List;


public class FireResponseDTO {		// réponse globale (fiche habitant complète + n° de caserne)
    private String stationNumber;
    private List<FireResidentDTO> residents;
    
    
	public FireResponseDTO(String stationNumber, List<FireResidentDTO> residents) {
		super();
		this.stationNumber = stationNumber;
		this.residents = residents;
	}
	
	public String getStationNumber() {
		return stationNumber;
	}
	public void setStationNumber(String stationNumber) {
		this.stationNumber = stationNumber;
	}
	public List<FireResidentDTO> getResidents() {
		return residents;
	}
	public void setResidents(List<FireResidentDTO> residents) {
		this.residents = residents;
	}
}

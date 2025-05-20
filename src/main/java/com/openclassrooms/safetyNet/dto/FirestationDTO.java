package com.openclassrooms.safetyNet.dto;


public class FirestationDTO {
	private String address;
    private String station;
    
	public FirestationDTO(String address, String station) {
		super();
		this.setAddress(address);
		this.setStation(station);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}
    
    
}

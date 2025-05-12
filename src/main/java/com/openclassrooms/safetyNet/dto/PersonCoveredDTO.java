package com.openclassrooms.safetyNet.dto;


/**
 * DTO utilisé pour représenter une personne couverte par une station.
 * Contient les informations utiles à l'affichage sans exposer toute l'entité Person.
 */


public class PersonCoveredDTO {
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public PersonCoveredDTO(String firstName, String lastName, String address, String phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
	}
	
	
}

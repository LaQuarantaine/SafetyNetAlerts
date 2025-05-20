package com.openclassrooms.safetyNet.dto;

public class PersonUpdateDTO {
	private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public PersonUpdateDTO() {}

    public PersonUpdateDTO(String address, String city, String zip, String phone, String email) {
        this.setAddress(address);
        this.setCity(city);
        this.setZip(zip);
        this.setPhone(phone);
        this.setEmail(email);
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

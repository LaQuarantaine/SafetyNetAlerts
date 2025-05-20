package com.openclassrooms.safetyNet.dto;

import java.util.Objects;

public class CommunityEmailDTO {
	private String email;

	public CommunityEmailDTO(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommunityEmailDTO other = (CommunityEmailDTO) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return email;
	}
	
	
}

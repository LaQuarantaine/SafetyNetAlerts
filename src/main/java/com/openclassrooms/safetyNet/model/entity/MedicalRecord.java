package com.openclassrooms.safetyNet.model.entity;

import java.util.List;

import com.openclassrooms.safetyNet.util.StringListJsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "medicalrecords")
@Data
public class MedicalRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "lastName")
	private String lastName;
	
	private String birthdate;
	
	@ManyToOne
	private Person person;
	
	@Convert(converter = StringListJsonConverter.class)
	@Column(columnDefinition = "TEXT")
	private List<String> medications;
	
	@Convert(converter = StringListJsonConverter.class)
	@Column(columnDefinition = "TEXT")
	private List<String> allergies;
	
}

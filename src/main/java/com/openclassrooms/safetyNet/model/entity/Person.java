package com.openclassrooms.safetyNet.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 	
@Table(name = "persons")	
@Data	
@NoArgsConstructor
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "firstName")  
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    
    @OneToMany(mappedBy = "person")
	private List<MedicalRecord> medicalRecords = new ArrayList<>();
}
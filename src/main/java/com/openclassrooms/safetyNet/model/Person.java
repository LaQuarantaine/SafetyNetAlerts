package com.openclassrooms.safetyNet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 	//indique que la classe correspond à une table de la base de données.
@Table(name = "persons")	//indique le nom de la table associée
@Data	//Lombok, évite les getters et les setters
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
	
}
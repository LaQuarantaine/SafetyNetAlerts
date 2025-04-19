package com.openclassrooms.safetyNet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilisé pour représenter une personne couverte par une station.
 * Contient les informations utiles à l'affichage sans exposer toute l'entité Person.
 */

@Data	//Lombok
@NoArgsConstructor	//génère un constructeur vide
@AllArgsConstructor	//génère une constructeur avec tous les champs en paramètres
public class PersonCoveredDTO {
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private int age;
	
}

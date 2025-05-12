package com.openclassrooms.safetyNet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO global de réponse

@Data	//Lombok
@NoArgsConstructor	//génère un constructeur vide
@AllArgsConstructor	//génère une constructeur avec tous les champs en paramètres
public class FirestationResponseDTO {
	private List<PersonCoveredDTO> persons;
	private int adultCount;
	private int childCount;

}



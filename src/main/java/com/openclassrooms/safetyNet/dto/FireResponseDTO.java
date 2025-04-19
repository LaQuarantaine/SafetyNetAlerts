package com.openclassrooms.safetyNet.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireResponseDTO {		// réponse globale (fiche habitant complète + n° de caserne)
    private String stationNumber;
    private List<FireResidentDTO> residents;
}

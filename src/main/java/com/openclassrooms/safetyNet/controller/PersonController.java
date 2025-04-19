package com.openclassrooms.safetyNet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.PersonInfoDTO;
import com.openclassrooms.safetyNet.service.FirestationLogicService;
import com.openclassrooms.safetyNet.service.PersonInfoService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PersonController {
	private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	private final PersonInfoService personInfoService;
	private final FirestationLogicService firestationLogicService; 

	
	@GetMapping("/childAlert")	// endpoint 2
	public ResponseEntity<?> getChildAlert(@RequestParam String address) {
		logger.info("Entrée requête GET /childAlert?address={}", address);
	    List<ChildAlertDTO> result = personInfoService.getChildrenByAddress(address);
	    if (result.isEmpty()) {	// Gère le cas où aucun enfant n’est trouvé
	    	logger.info("Aucun enfant trouvé pour l'adresse : {}", address);
	        return ResponseEntity.ok("No children found at this address."); // renvoi une chaine vide " "
	    }
	    logger.info("{} enfant(s) trouvé(s) à l'adresse : {}", result.size(), address);
	    return ResponseEntity.ok(result); // si ok renvoi la liste en JSON
	}
	
	@GetMapping("/fire")	// endpoint 4
    public ResponseEntity<FireResponseDTO> getFireInfo(@RequestParam String address) {
        log.info("Requête GET /person/fire?address={}", address);
        FireResponseDTO response = firestationLogicService.getFireAlert(address);
        log.info("Réponse avec {} résident(s) à l'adresse {}", response.getResidents() !=null ? response.getResidents().size() : 0, address);
        return ResponseEntity.ok(response);
    }
    
	@GetMapping("/personInfo")	// endpoint 6
	public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam String lastName) {
	    log.info("Requête GET /personInfo?lastName={}", lastName);
	    List<PersonInfoDTO> persons = personInfoService.getPersonsByLastName(lastName);
	    log.info("Réponse avec {} personne(s) trouvée(s)", persons.size());
	    return ResponseEntity.ok(persons);
	}
}

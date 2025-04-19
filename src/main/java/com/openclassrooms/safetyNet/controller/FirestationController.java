package com.openclassrooms.safetyNet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.dto.FirestationResponseDTO;
import com.openclassrooms.safetyNet.service.FirestationLogicService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// le contrôleur REST
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class FirestationController {
	
	private final FirestationLogicService firestationLogicService;

	
	@GetMapping("/firestation") // endpoint 1
	public ResponseEntity<FirestationResponseDTO> getByStation(@RequestParam String stationNumber) {
		log.info("Entrée requête GET /firestation?stationNumber={}", stationNumber);
		FirestationResponseDTO response = firestationLogicService.getPersonsByStation(stationNumber);
		log.info("Sortie réponse avec {} personnes", response.getPersons().size());
		return ResponseEntity.ok(response);
	}
	
    
	@GetMapping("/phoneAlert")	// endpoint 3
	public ResponseEntity<List<String>> getPhoneAlert(@RequestParam(name = "firestation") String stationNumber) {
	    log.info("Entrée requête GET /phoneAlert?firestation={}", stationNumber);
	    List<String> phones = firestationLogicService.getPhoneNumbersByStation(stationNumber);
	    log.info("Sortie réponse avec {} numéro(s) de téléphone", phones.size());
	    return ResponseEntity.ok(phones);
	}
	
	@GetMapping("/flood/stations")	// endpoint 5
	public ResponseEntity<Map<String, List<FireResidentDTO>>> getFloodInfo(@RequestParam List<String> stations) {
	    log.info("Requête GET /flood/stations?stations={}", stations);
	    Map<String, List<FireResidentDTO>> response = firestationLogicService.getFloodInfoByStations(stations);
	    log.info("Réponse avec {} adresse(s) impactée(s)", response.size());
	    return ResponseEntity.ok(response);
	}

}

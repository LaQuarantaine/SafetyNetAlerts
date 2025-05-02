package com.openclassrooms.safetyNet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.CommunityEmailDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.FireResidentDTO;
import com.openclassrooms.safetyNet.service.FirestationLogicService;
import com.openclassrooms.safetyNet.service.PersonInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AlertController {

    private final FirestationLogicService firestationLogicService;
    private final PersonInfoService personInfoService;
	
	@GetMapping("/childAlert")	// endpoint 2
	public ResponseEntity<?> getChildAlert(@RequestParam String address) {
		log.info("Entrée requête GET /childAlert?address={}", address);
	    List<ChildAlertDTO> result = personInfoService.getChildrenByAddress(address);
	    if (result.isEmpty()) {	// Gère le cas où aucun enfant n’est trouvé
	    	log.info("Aucun enfant trouvé pour l'adresse : {}", address);
	        return ResponseEntity.ok("No children found at this address."); // renvoi une chaine vide " "
	    }
	    log.info("{} enfant(s) trouvé(s) à l'adresse : {}", result.size(), address);
	    return ResponseEntity.ok(result); // si ok renvoi la liste en JSON
	}
	

	@GetMapping("/phoneAlert")	// endpoint 3
	public ResponseEntity<List<String>> getPhoneAlert(@RequestParam(name = "firestation") String stationNumber) {
	    log.info("Entrée requête GET /phoneAlert?firestation={}", stationNumber);
	    List<String> phones = firestationLogicService.getPhoneNumbersByStation(stationNumber);
	    log.info("Sortie réponse avec {} numéro(s) de téléphone", phones.size());
	    return ResponseEntity.ok(phones);
	}
	
	
	@GetMapping("/fire")	// endpoint 4
    public ResponseEntity<FireResponseDTO> getFireInfo(@RequestParam String address) {
        log.info("Requête GET /person/fire?address={}", address);
        FireResponseDTO response = firestationLogicService.getFireAlert(address);
        log.info("Réponse avec {} résident(s) à l'adresse {}", response.getResidents() !=null ? response.getResidents().size() : 0, address);
        return ResponseEntity.ok(response);
    }
	
	
	@GetMapping("/flood/stations")	// endpoint 5
	public ResponseEntity<Map<String, List<FireResidentDTO>>> getFloodInfo(@RequestParam List<String> stations) {
	    log.info("Requête GET /flood/stations?stations={}", stations);
	    Map<String, List<FireResidentDTO>> response = firestationLogicService.getFloodInfoByStations(stations);
	    log.info("Réponse avec {} adresse(s) impactée(s)", response.size());
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/communityEmail")	// endpoint 7
	public ResponseEntity<List<CommunityEmailDTO>> getCommunityEmail(@RequestParam String city) {
		log.info("Requete GET /communityEmail?city={}", city);
		List<CommunityEmailDTO> emails = personInfoService.getEmailByCity(city);
		log.info("Réponse avec {} email(s) trouvé(s)", emails.size());
		return ResponseEntity.ok(emails);
	}
	

}

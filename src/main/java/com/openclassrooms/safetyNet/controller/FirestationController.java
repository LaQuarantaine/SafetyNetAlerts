package com.openclassrooms.safetyNet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetyNet.dto.FirestationDTO;
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

	// POST : Ajouter un mapping adresse/caserne
    @PostMapping("/firestation")
    public ResponseEntity<?> addMapping(@RequestBody FirestationDTO firestationDTO) {
        log.info("POST /firestation avec données: {}", firestationDTO);
        firestationLogicService.addFirestationMapping(firestationDTO);
        return ResponseEntity.ok().build();
    }

    // PUT : Mettre à jour le numéro de caserne d'une adresse
    @PutMapping("/firestation")
    public ResponseEntity<?> updateMapping(@RequestParam String address, @RequestParam String newStation) {
        log.info("PUT /firestation pour address={} et nouveau numéro={}", address, newStation);
        boolean updated = firestationLogicService.updateFirestationMapping(address, newStation);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/firestation/address")
    public ResponseEntity<?> deleteMappingByAddress(@RequestParam String address) {
        log.info("DELETE /firestation/address pour address={}", address);
        boolean deleted = firestationLogicService.deleteFirestationByAddress(address);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/firestation/station")
    public ResponseEntity<?> deleteMappingByStation(@RequestParam String station) {
        log.info("DELETE /firestation/station pour station={}", station);
        boolean deleted = firestationLogicService.deleteFirestationByStation(station);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

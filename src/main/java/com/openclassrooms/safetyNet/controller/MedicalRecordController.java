package com.openclassrooms.safetyNet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetyNet.dto.MedicalRecordDTO;
import com.openclassrooms.safetyNet.service.MedicalRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordController {

	private final MedicalRecordService medicalRecordService;
	
	@PostMapping("/medicalRecord")
	public ResponseEntity<?> addMedicalRecord(@RequestParam String firstName,
	                                          @RequestParam String lastName,
	                                          @RequestBody MedicalRecordDTO dto) {
	    log.info("POST /medicalRecord pour {} {}", firstName, lastName);

	    dto.setFirstName(firstName);
	    dto.setLastName(lastName);

	    boolean created = medicalRecordService.addMedicalRecord(dto);
	    return created
	            ? ResponseEntity.ok().build()
	            : ResponseEntity.status(HttpStatus.CONFLICT).body("Medical record already exists or person not found");
	}

	@PutMapping("/medicalRecord")
	public ResponseEntity<?> updateMedicalRecord(@RequestParam String firstName,
	                                             @RequestParam String lastName,
	                                             @RequestBody MedicalRecordDTO dto) {
	    log.info("PUT /medicalRecord pour {} {}", firstName, lastName);

	    // On force les noms reçus dans l'URL pour éviter des incohérences
	    dto.setFirstName(firstName);
	    dto.setLastName(lastName);

	    boolean updated = medicalRecordService.updateMedicalRecord(dto);
	    return updated
	            ? ResponseEntity.ok().build()
	            : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/medicalRecord")
	public ResponseEntity<?> deleteMedicalRecord(@RequestParam String firstName,
	                                             @RequestParam String lastName) {
	    log.info("DELETE /medicalRecord pour {} {}", firstName, lastName);

	    boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
	    return deleted
	            ? ResponseEntity.ok().build()
	            : ResponseEntity.notFound().build();
	}
}


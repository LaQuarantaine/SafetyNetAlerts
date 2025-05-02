package com.openclassrooms.safetyNet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetyNet.dto.PersonDTO;
import com.openclassrooms.safetyNet.dto.PersonInfoDTO;
import com.openclassrooms.safetyNet.model.entity.Person;
import com.openclassrooms.safetyNet.service.PersonInfoService;
import com.openclassrooms.safetyNet.service.PersonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PersonController {
	private final PersonInfoService personInfoService;
	private final PersonService personService;

	    
	@GetMapping("/personInfo")	// endpoint 6
	public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam String lastName) {
	    log.info("Requête GET /personInfo?lastName={}", lastName);
	    List<PersonInfoDTO> persons = personInfoService.getPersonsByLastName(lastName);
	    log.info("Réponse avec {} personne(s) trouvée(s)", persons.size());
	    return ResponseEntity.ok(persons);
	}
	

	@PostMapping("/person")    // POST : Ajouter une personne
	public ResponseEntity<Person> addPerson(@RequestBody PersonDTO personDTO) {
	    log.info("Requête POST /person avec données : {}", personDTO);
	    Person createdPerson = personService.addPerson(personDTO);
	    return ResponseEntity.ok(createdPerson);
	}

	
	@PutMapping("/person")     // PUT : Mettre à jour une personne
	public ResponseEntity<?> updatePerson(@RequestParam String firstName,
	                                      @RequestParam String lastName,
	                                      @RequestBody PersonDTO personDTO) {
	    log.info("Requête PUT /person pour {} {}", firstName, lastName);
	    return personService.updatePerson(firstName, lastName, personDTO)
	            .map(updated -> ResponseEntity.ok(updated))
	            .orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	
	@DeleteMapping("/person")   // DELETE : Supprimer une personne
	public ResponseEntity<?> deletePerson(@RequestParam String firstName,
	                                      @RequestParam String lastName) {
	    log.info("Requête DELETE /person pour {} {}", firstName, lastName);
	    boolean deleted = personService.deletePerson(firstName, lastName);
	    if (deleted) {
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
}

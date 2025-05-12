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

import com.openclassrooms.safetyNet.dto.ChildAlertDTO;
import com.openclassrooms.safetyNet.dto.CommunityEmailDTO;
import com.openclassrooms.safetyNet.dto.FireResponseDTO;
import com.openclassrooms.safetyNet.dto.PersonDTO;
import com.openclassrooms.safetyNet.dto.PersonInfoDTO;
import com.openclassrooms.safetyNet.service.FirestationLogicService;
import com.openclassrooms.safetyNet.service.PersonInfoService;
import com.openclassrooms.safetyNet.service.PersonService;



@RestController
@RequestMapping
public class PersonController {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PersonController.class);
	
	private final PersonInfoService personInfoService;
	private final PersonService personService;
	private final FirestationLogicService firestationLogicService; 
	

	public PersonController(PersonInfoService personInfoService, PersonService personService,
			FirestationLogicService firestationLogicService) {
		super();
		this.personInfoService = personInfoService;
		this.personService = personService;
		this.firestationLogicService = firestationLogicService;
	}

	
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
	
	@GetMapping("/communityEmail")	// endpoint 7
	public ResponseEntity<List<CommunityEmailDTO>> getCommunityEmail(@RequestParam String city) {
		log.info("Requete GET /communityEmail?city={}", city);
		List<CommunityEmailDTO> emails = personInfoService.getEmailByCity(city);
		log.info("Réponse avec {} email(s) trouvé(s)", emails.size());
		return ResponseEntity.ok(emails);
	}
	
	
	@PostMapping("/person")    // POST : Ajouter une personne
	public ResponseEntity<PersonDTO> addPerson(@RequestBody PersonDTO personDTO) {
	    log.info("Requête POST /person avec données : {}", personDTO);
	    PersonDTO createdPerson = personService.addPerson(personDTO);
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
